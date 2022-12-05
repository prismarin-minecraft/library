package in.prismar.library.spigot.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * A Spigot util to easily make entities glow.
 * <p>
 * <b>1.17 -> 1.19.2</b>
 * 
 * @version 1.1.2
 * @author SkytAsul
 */
public class GlowingEntities implements Listener {
	
	private Map<Player, PlayerData> glowing;
	private boolean enabled = false;
	
	private int uuid;
	
	/**
	 * Initializes the Glowing API.
	 * 
	 * @param plugin plugin that will be used to register the events.
	 */
	public GlowingEntities(Plugin plugin) {
		if (!Packets.enabled) throw new IllegalStateException("The Glowing Entities API is disabled. An error has occured during initialization.");
		enable(plugin);
	}
	
	/**
	 * Enables the Glowing API.
	 * 
	 * @param plugin plugin that will be used to register the events.
	 * @see #disable()
	 */
	public void enable(Plugin plugin) {
		if (enabled) throw new IllegalStateException("The Glowing Entities API has already been enabled.");
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		glowing = new HashMap<>();
		uuid = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
		enabled = true;
	}
	
	/**
	 * Disables the API.
	 * <p>
	 * Methods such as {@link #setGlowing(int, String, Player, ChatColor, byte)}
	 * and {@link #unsetGlowing(int, Player)} will no longer be usable.
	 * 
	 * @see #enable(Plugin)
	 */
	public void disable() {
		if (!enabled) return;
		HandlerList.unregisterAll(this);
		glowing.values().forEach(playerData -> {
			try {
				Packets.removePacketsHandler(playerData);
			}catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
		});
		glowing = null;
		uuid = 0;
		enabled = false;
	}
	
	private void ensureEnabled() {
		if (!enabled) throw new IllegalStateException("The Glowing Entities API is not enabled.");
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		glowing.remove(event.getPlayer());
	}
	
	/**
	 * Make the {@link Entity} passed as a parameter glow with its default team color.
	 * 
	 * @param entity entity to make glow
	 * @param receiver player which will see the entity glowing
	 * @throws ReflectiveOperationException
	 */
	public void setGlowing(Entity entity, Player receiver) throws ReflectiveOperationException {
		setGlowing(entity, receiver, null);
	}
	
	/**
	 * Make the {@link Entity} passed as a parameter glow with the specified color.
	 * 
	 * @param entity entity to make glow
	 * @param receiver player which will see the entity glowing
	 * @param color color of the glowing effect
	 * @throws ReflectiveOperationException
	 */
	public void setGlowing(Entity entity, Player receiver, ChatColor color) throws ReflectiveOperationException {
		String teamID = entity instanceof Player ? entity.getName() : entity.getUniqueId().toString();
		setGlowing(entity.getEntityId(), teamID, receiver, color, Packets.getEntityFlags(entity));
	}
	
	/**
	 * Make the entity with specified entity ID glow with its default team color.
	 * 
	 * @param entityID entity id of the entity to make glow
	 * @param teamID internal string used to add the entity to a team
	 * @param receiver player which will see the entity glowing
	 * @throws ReflectiveOperationException
	 */
	public void setGlowing(int entityID, String teamID, Player receiver) throws ReflectiveOperationException {
		setGlowing(entityID, teamID, receiver, null, (byte) 0);
	}
	
	/**
	 * Make the entity with specified entity ID glow with the specified color.
	 * 
	 * @param entityID entity id of the entity to make glow
	 * @param teamID internal string used to add the entity to a team
	 * @param receiver player which will see the entity glowing
	 * @param color color of the glowing effect
	 * @throws ReflectiveOperationException
	 */
	public void setGlowing(int entityID, String teamID, Player receiver, ChatColor color) throws ReflectiveOperationException {
		setGlowing(entityID, teamID, receiver, color, (byte) 0);
	}
	
	/**
	 * Make the entity with specified entity ID glow with the specified color, and keep some flags.
	 * 
	 * @param entityID entity id of the entity to make glow
	 * @param teamID internal string used to add the entity to a team
	 * @param receiver player which will see the entity glowing
	 * @param color color of the glowing effect
	 * @param otherFlags internal flags that must be kept (on fire, crouching...).
	 * See <a href="https://wiki.vg/Entity_metadata#Entity">wiki.vg</a> for more informations.
	 * @throws ReflectiveOperationException
	 */
	public void setGlowing(int entityID, String teamID, Player receiver, ChatColor color, byte otherFlags) throws ReflectiveOperationException {
		ensureEnabled();
		if (color != null && !color.isColor()) throw new IllegalArgumentException("ChatColor must be a color format");
		
		PlayerData playerData = glowing.get(receiver);
		if (playerData == null) {
			playerData = new PlayerData(this, receiver);
			Packets.addPacketsHandler(playerData);
			glowing.put(receiver, playerData);
		}
		
		GlowingData glowingData = playerData.glowingDatas.get(entityID);
		if (glowingData == null) {
			// the player did not have datas related to the entity: we must create the glowing status
			glowingData = new GlowingData(playerData, entityID, teamID, color, otherFlags);
			playerData.glowingDatas.put(entityID, glowingData);
			
			Packets.createGlowing(glowingData);
			if (color != null) Packets.setGlowingColor(glowingData);
		}else {
			// the player already had datas related to the entity: we must update the glowing status
			
			if (Objects.equals(glowingData.color, color)) return; // nothing changed
			
			if (color == null) {
				Packets.removeGlowingColor(glowingData);
				glowingData.color = color; // we must set the color after in order to fetch the previous team
			}else {
				glowingData.color = color;
				Packets.setGlowingColor(glowingData);
			}
		}
	}
	
	/**
	 * Make the {@link Entity} passed as a parameter loose its custom glowing effect.
	 * <p>
	 * This has <b>no effect</b> on glowing status given by another plugin or vanilla behavior.
	 * 
	 * @param entity entity to remove glowing effect from
	 * @param receiver player which will no longer see the glowing effect
	 * @throws ReflectiveOperationException
	 */
	public void unsetGlowing(Entity entity, Player receiver) throws ReflectiveOperationException {
		unsetGlowing(entity.getEntityId(), receiver);
	}
	
	/**
	 * Make the entity with specified entity ID passed as a parameter loose its custom glowing effect.
	 * <p>
	 * This has <b>no effect</b> on glowing status given by another plugin or vanilla behavior.
	 * 
	 * @param entityID entity id of the entity to remove glowing effect from
	 * @param receiver player which will no longer see the glowing effect
	 * @throws ReflectiveOperationException
	 */
	public void unsetGlowing(int entityID, Player receiver) throws ReflectiveOperationException {
		ensureEnabled();
		PlayerData playerData = glowing.get(receiver);
		if (playerData == null) return; // the player do not have any entity glowing
		
		GlowingData glowingData = playerData.glowingDatas.remove(entityID);
		if (glowingData == null) return; // the player did not have this entity glowing
		
		Packets.removeGlowing(glowingData);
		
		if (glowingData.color != null) Packets.removeGlowingColor(glowingData);
		
		/* if (playerData.glowingDatas.isEmpty()) { //NOSONAR
			// if the player do not have any other entity glowing,
			// we can safely remove all of its data to free some memory
			Packets.removePacketsHandler(playerData);
			glowing.remove(receiver);
		} */
		// actually no, we should not remove the player datas
		// as it stores which teams did it receive.
		// if we do not save this information, team would be created
		// twice for the player, and BungeeCord does not like that
	}
	
	private static class PlayerData {
		
		final GlowingEntities instance;
		final Player player;
		final Map<Integer, GlowingData> glowingDatas;
		ChannelHandler packetsHandler;
		EnumSet<ChatColor> sentColors;
		
		PlayerData(GlowingEntities instance, Player player) {
			this.instance = instance;
			this.player = player;
			this.glowingDatas = new HashMap<>();
		}
		
	}
	
	private static class GlowingData {
		// unfortunately this cannot be a Java Record
		// as the "color" field is not final
		
		final PlayerData player;
		final int entityID;
		final String teamID;
		ChatColor color;
		byte otherFlags;
		boolean enabled;
		
		GlowingData(PlayerData player, int entityID, String teamID, ChatColor color, byte otherFlags) {
			this.player = player;
			this.entityID = entityID;
			this.teamID = teamID;
			this.color = color;
			this.otherFlags = otherFlags;
			this.enabled = true;
		}
		
	}
	
	private static class Packets {
		
		private static final byte GLOWING_FLAG = 1 << 6;
		
		private static Cache<Object, Object> packets = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).build();
		private static Object dummy = new Object();
		
		private static Logger logger;
		private static int version;
		private static int versionMinor;
		private static String cpack = Bukkit.getServer().getClass().getPackage().getName() + ".";
		private static ProtocolMappings mappings;
		public static boolean enabled = false;
		
		private static Method getHandle;
		private static Method getDataWatcher;
		
		private static Object watcherObjectFlags;
		private static Object watcherDummy;
		private static Method watcherGet;
		private static Constructor<?> watcherItemConstructor;
		private static Method watcherItemObject;
		private static Method watcherItemDataGet;
		
		private static Field playerConnection;
		private static Method sendPacket;
		private static Field networkManager;
		private static Field channelField;
		
		private static Class<?> packetMetadata;
		private static Constructor<?> packetMetadataConstructor;
		private static Field packetMetadataEntity;
		private static Field packetMetadataItems;
		
		private static EnumMap<ChatColor, TeamData> teams = new EnumMap<>(ChatColor.class);
		
		private static Constructor<?> createTeamPacket;
		private static Constructor<?> createTeamPacketData;
		private static Constructor<?> createTeam;
		private static Object scoreboardDummy;
		private static Object pushNever;
		private static Method setTeamPush;
		private static Method setTeamColor;
		private static Method getColorConstant;
		
		static {
			try {
				logger = new Logger("GlowingEntities", null) {
					@Override
					public void log(LogRecord logRecord) {
						logRecord.setMessage("[GlowingEntities] " + logRecord.getMessage());
						super.log(logRecord);
					}
				};
				logger.setParent(Bukkit.getServer().getLogger());
				logger.setLevel(Level.ALL);
				
				// e.g. Bukkit.getServer().getClass().getPackage().getName() -> org.bukkit.craftbukkit.v1_17_R1
				String[] versions = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1).split("_");
				version = Integer.parseInt(versions[1]); // 1.X
				// e.g. Bukkit.getBukkitVersion() -> 1.17.1-R0.1-SNAPSHOT
				versions = Bukkit.getBukkitVersion().split("-R")[0].split("\\.");
				versionMinor = versions.length <= 2 ? 0 : Integer.parseInt(versions[2]);
				logger.info("Found server version 1." + version + "." + versionMinor);
				
				mappings = ProtocolMappings.getMappings(version);
				if (mappings == null) {
					mappings = ProtocolMappings.values()[ProtocolMappings.values().length - 1];
					logger.warning("Loaded not matching version of the mappings for your server version (1." + version + "." + versionMinor + ")");
				}
				logger.info("Loaded mappings " + mappings.name());
				
				/* Global variables */

				Class<?> entityClass = getNMSClass("world.entity", "Entity");
				Class<?> entityTypesClass = getNMSClass("world.entity", "EntityTypes");
				Object markerEntity = getNMSClass("world.entity", "Marker").getDeclaredConstructors()[0].newInstance(getField(entityTypesClass, mappings.getMarkerTypeId(), null), null);
				
				getHandle = getCraftClass("entity", "CraftEntity").getDeclaredMethod("getHandle");
				getDataWatcher = entityClass.getDeclaredMethod(mappings.getWatcherAccessor());
				
				/* DataWatchers */
				
				Class<?> dataWatcherClass = getNMSClass("network.syncher", "DataWatcher");
				
				watcherObjectFlags = getField(entityClass, mappings.getWatcherFlags(), null);
				watcherDummy = dataWatcherClass.getDeclaredConstructor(entityClass).newInstance(markerEntity);
				watcherGet = version >= 18 ? dataWatcherClass.getDeclaredMethod("a", watcherObjectFlags.getClass()) : getMethod(dataWatcherClass, "get");
				
				Class<?> watcherItem = getNMSClass("network.syncher", "DataWatcher$Item");
				watcherItemConstructor = watcherItem.getDeclaredConstructor(watcherObjectFlags.getClass(), Object.class);
				watcherItemObject = watcherItem.getDeclaredMethod("a");
				watcherItemDataGet = watcherItem.getDeclaredMethod("b");
				
				/* Connections */
				
				playerConnection = getNMSClass("server.level", "EntityPlayer").getDeclaredField(mappings.getPlayerConnection());
				sendPacket = getNMSClass("server.network", "PlayerConnection").getMethod(mappings.getSendPacket(), getNMSClass("network.protocol", "Packet"));
				networkManager = getNMSClass("server.network", "PlayerConnection").getDeclaredField(mappings.getNetworkManager());
				channelField = getNMSClass("network", "NetworkManager").getDeclaredField(mappings.getChannel());
				
				/* Metadata */
				
				packetMetadata = getNMSClass("network.protocol.game", "PacketPlayOutEntityMetadata");
				packetMetadataConstructor = packetMetadata.getDeclaredConstructor(int.class, dataWatcherClass, boolean.class);
				packetMetadataEntity = getField(packetMetadata, "a");
				packetMetadataItems = getField(packetMetadata, "b");
				
				/* Teams */
				
				Class<?> scoreboardClass = getNMSClass("world.scores", "Scoreboard");
				Class<?> teamClass = getNMSClass("world.scores", "ScoreboardTeam");
				Class<?> pushClass = getNMSClass("world.scores", "ScoreboardTeamBase$EnumTeamPush");
				Class<?> chatFormatClass = getNMSClass("EnumChatFormat");
				
				createTeamPacket = getNMSClass("network.protocol.game", "PacketPlayOutScoreboardTeam").getDeclaredConstructor(String.class, int.class, Optional.class, Collection.class);
				createTeamPacket.setAccessible(true);
				createTeamPacketData = getNMSClass("network.protocol.game", "PacketPlayOutScoreboardTeam$b").getDeclaredConstructor(teamClass);
				createTeam = teamClass.getDeclaredConstructor(scoreboardClass, String.class);
				scoreboardDummy = scoreboardClass.getDeclaredConstructor().newInstance();
				pushNever = pushClass.getDeclaredField("b").get(null);
				setTeamPush = teamClass.getDeclaredMethod(mappings.getTeamSetCollision(), pushClass);
				setTeamColor = teamClass.getDeclaredMethod(mappings.getTeamSetColor(), chatFormatClass);
				getColorConstant = chatFormatClass.getDeclaredMethod("a", char.class);
				
				enabled = true;
			}catch (Exception ex) {
				String errorMsg = "Laser Beam reflection failed to initialize. The util is disabled. Please ensure your version (" + Bukkit.getServer().getClass().getPackage().getName() + ") is supported.";
				if (logger == null) {
					ex.printStackTrace();
					System.err.println(errorMsg);
				}else {
					logger.log(Level.SEVERE, errorMsg, ex);
				}
			}
		}

		public static void sendPackets(Player p, Object... packets) throws ReflectiveOperationException {
			Object connection = playerConnection.get(getHandle.invoke(p));
			for (Object packet : packets) {
				if (packet == null) continue;
				sendPacket.invoke(connection, packet);
			}
		}
		
		public static byte getEntityFlags(Entity entity) throws ReflectiveOperationException {
			Object nmsEntity = getHandle.invoke(entity);
			Object dataWatcher = getDataWatcher.invoke(nmsEntity);
			return (byte) watcherGet.invoke(dataWatcher, watcherObjectFlags);
		}
		
		public static void createGlowing(GlowingData glowingData) throws ReflectiveOperationException {
			setMetadata(glowingData, computeFlags(glowingData));
		}
		
		private static byte computeFlags(GlowingData glowingData) {
			byte newFlags = glowingData.otherFlags;
			if (glowingData.enabled) {
				newFlags |= GLOWING_FLAG;
			}else {
				newFlags &= ~GLOWING_FLAG;
			}
			return newFlags;
		}
		
		public static void removeGlowing(GlowingData glowingData) throws ReflectiveOperationException {
			setMetadata(glowingData, glowingData.otherFlags);
		}

		@SuppressWarnings ("squid:S3011")
		private static void setMetadata(GlowingData glowingData, byte flags) throws ReflectiveOperationException {
			List<Object> dataItems = new ArrayList<>(1);
			dataItems.add(watcherItemConstructor.newInstance(watcherObjectFlags, flags));
			
			Object packetMetadata = packetMetadataConstructor.newInstance(glowingData.entityID, watcherDummy, false);
			packetMetadataItems.set(packetMetadata, dataItems);
			packets.put(packetMetadata, dummy);
			sendPackets(glowingData.player.player, packetMetadata);
		}
		
		public static void setGlowingColor(GlowingData glowingData) throws ReflectiveOperationException {
			boolean sendCreation = false;
			if (glowingData.player.sentColors == null) {
				glowingData.player.sentColors = EnumSet.of(glowingData.color);
				sendCreation = true;
			}else if (glowingData.player.sentColors.add(glowingData.color)) {
				sendCreation = true;
			}
			
			TeamData teamData = teams.get(glowingData.color);
			if (teamData == null) {
				teamData = new TeamData(glowingData.player.instance.uuid, glowingData.color);
				teams.put(glowingData.color, teamData);
			}
			
			Object entityAddPacket = teamData.getEntityAddPacket(glowingData.teamID);
			if (sendCreation) {
				sendPackets(glowingData.player.player, teamData.creationPacket, entityAddPacket);
			}else {
				sendPackets(glowingData.player.player, entityAddPacket);
			}
		}
		
		public static void removeGlowingColor(GlowingData glowingData) throws ReflectiveOperationException {
			TeamData teamData = teams.get(glowingData.color);
			if (teamData == null) return; // must not happen; this means the color has not been set previously
			
			sendPackets(glowingData.player.player, teamData.getEntityRemovePacket(glowingData.teamID));
		}
		
		private static Channel getChannel(Player player) throws ReflectiveOperationException {
			return (Channel) channelField.get(networkManager.get(playerConnection.get(getHandle.invoke(player))));
		}
		
		public static void addPacketsHandler(PlayerData playerData) throws ReflectiveOperationException {
			playerData.packetsHandler = new ChannelDuplexHandler() {
				@Override
				public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
					if (msg.getClass().equals(packetMetadata) && packets.asMap().remove(msg) == null) {
						int entityID = packetMetadataEntity.getInt(msg);
						GlowingData glowingData = playerData.glowingDatas.get(entityID);
						if (glowingData != null) {
							
							List<Object> items = (List<Object>) packetMetadataItems.get(msg);
							if (items != null) {
								
								List<Object> copy = null;
								for (int i = 0; i < items.size(); i++) {
									Object item = items.get(i);
									if (watcherItemObject.invoke(item).equals(watcherObjectFlags)) {
										byte flags = (byte) watcherItemDataGet.invoke(item);
										glowingData.otherFlags = flags;
										byte newFlags = computeFlags(glowingData);
										if (newFlags != flags) {
											if (copy == null) copy = new ArrayList<>(items);
											copy.set(i, watcherItemConstructor.newInstance(watcherObjectFlags, newFlags));
											// we cannot simply edit the item as it may be backed in the datawatcher
										}
									}
								}
								
								if (copy != null) {
									// some of the metadata packets are broadcasted to all players near the target entity.
									// hence, if we directly edit the packet, some users that were not intended to see the
									// glowing color will be able to see it. We should send a new packet to the viewer only.
									
									Object newMsg = packetMetadataConstructor.newInstance(entityID, watcherDummy, false);
									packetMetadataItems.set(newMsg, copy);
									packets.put(newMsg, dummy);
									sendPackets(playerData.player, newMsg);
									
									return; // we cancel the send of this packet
								}
							}
						}
					}
					super.write(ctx, msg, promise);
				}
			};
			
			getChannel(playerData.player).pipeline().addBefore("packet_handler", null, playerData.packetsHandler);
		}
		
		public static void removePacketsHandler(PlayerData playerData) throws ReflectiveOperationException {
			if (playerData.packetsHandler != null) {
				getChannel(playerData.player).pipeline().remove(playerData.packetsHandler);
			}
		}
		
		/* Reflection utils */
		private static Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(name)) return m;
			}
			throw new NoSuchMethodException(name + " in " + clazz.getName());
		}
		
		@Deprecated
		private static Object getField(Class<?> clazz, String name, Object instance) throws ReflectiveOperationException {
			return getField(clazz, name).get(instance);
		}
		
		private static Field getField(Class<?> clazz, String name) throws ReflectiveOperationException {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field;
		}
		
		private static Class<?> getCraftClass(String craftPackage, String className) throws ClassNotFoundException {
			return Class.forName(cpack + craftPackage + "." + className);
		}
		
		private static Class<?> getNMSClass(String className) throws ClassNotFoundException {
			return Class.forName("net.minecraft." + className);
		}
		
		private static Class<?> getNMSClass(String nmPackage, String className) throws ClassNotFoundException {
			return Class.forName("net.minecraft." + nmPackage + "." + className);
		}
		
		private static class TeamData {
			
			private final String id;
			private final Object creationPacket;
			
			private final Cache<String, Object> addPackets = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
			private final Cache<String, Object> removePackets = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.MINUTES).build();
			
			public TeamData(int uuid, ChatColor color) throws ReflectiveOperationException {
				if (!color.isColor()) throw new IllegalArgumentException();
				id = "glow-" + uuid + color.getChar();
				Object team = createTeam.newInstance(scoreboardDummy, id);
				setTeamPush.invoke(team, pushNever);
				setTeamColor.invoke(team, getColorConstant.invoke(null, color.getChar()));
				Object packetData = createTeamPacketData.newInstance(team);
				creationPacket = createTeamPacket.newInstance(id, 0, Optional.of(packetData), Collections.EMPTY_LIST);
			}
			
			public Object getEntityAddPacket(String teamID) throws ReflectiveOperationException {
				Object packet = addPackets.getIfPresent(teamID);
				if (packet == null) {
					packet = createTeamPacket.newInstance(id, 3, Optional.empty(), Arrays.asList(teamID));
					addPackets.put(teamID, packet);
				}
				return packet;
			}
			
			public Object getEntityRemovePacket(String teamID) throws ReflectiveOperationException {
				Object packet = removePackets.getIfPresent(teamID);
				if (packet == null) {
					packet = createTeamPacket.newInstance(id, 4, Optional.empty(), Arrays.asList(teamID));
					removePackets.put(teamID, packet);
				}
				return packet;
			}
			
		}
		
		private enum ProtocolMappings {
			
			V1_17(
					17,
					"Z",
					"Y",
					"getDataWatcher",
					"b",
					"a",
					"sendPacket",
					"k",
					"setCollisionRule",
					"setColor"),
			V1_18(
					18,
					"Z",
					"Y",
					"ai",
					"b",
					"a",
					"a",
					"m",
					"a",
					"a"),
			V1_19(
					19,
					"Z",
					"ab",
					"ai",
					"b",
					"b",
					"a",
					"m",
					"a",
					"a"),
			;

			private final int major;
			private final String watcherFlags;
			private String markerTypeId;
			private String watcherAccessor;
			private String playerConnection;
			private String networkManager;
			private String sendPacket;
			private String channel;
			private String teamSetCollsion;
			private String teamSetColor;

			private ProtocolMappings(int major, String watcherFlags, String markerTypeId, String watcherAccessor, String playerConnection, String networkManager, String sendPacket, String channel, String teamSetCollsion, String teamSetColor) {
				this.major = major;
				this.watcherFlags = watcherFlags;
				this.markerTypeId = markerTypeId;
				this.watcherAccessor = watcherAccessor;
				this.playerConnection = playerConnection;
				this.networkManager = networkManager;
				this.sendPacket = sendPacket;
				this.channel = channel;
				this.teamSetCollsion = teamSetCollsion;
				this.teamSetColor = teamSetColor;
			}
			
			public int getMajor() {
				return major;
			}
			
			public String getWatcherFlags() {
				return watcherFlags;
			}
			
			public String getMarkerTypeId() {
				return markerTypeId;
			}
			
			public String getWatcherAccessor() {
				return watcherAccessor;
			}
			
			public String getPlayerConnection() {
				return playerConnection;
			}
			
			public String getNetworkManager() {
				return networkManager;
			}
			
			public String getSendPacket() {
				return sendPacket;
			}
			
			public String getChannel() {
				return channel;
			}
			
			public String getTeamSetCollision() {
				return teamSetCollsion;
			}
			
			public String getTeamSetColor() {
				return teamSetColor;
			}
			
			public static ProtocolMappings getMappings(int major) {
				for (ProtocolMappings map : values()) {
					if (major == map.getMajor()) return map;
				}
				return null;
			}
			
		}
		
	}
	
}