package in.prismar.library.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.prismar.library.common.repository.entity.RepositoryEntity;
import in.prismar.library.common.repository.impl.AbstractAsyncRepository;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HttpRepository<ID, E extends RepositoryEntity<ID>> extends AbstractAsyncRepository<ID, E> {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

    protected final String url;
    protected final String token;
    protected final Class<? extends E> entityClass;
    protected OkHttpClient client;
    protected Gson gson;

    public HttpRepository(String url, String token, Class<? extends E> entityClass, long delay) {
        super(entityClass.getSimpleName(), delay);
        this.url = url;
        this.token = token;
        this.entityClass = entityClass;
        this.client = new OkHttpClient();

        GsonBuilder builder = new GsonBuilder();
        intercept(builder);
        this.gson = builder.create();
    }

    public void intercept(GsonBuilder builder) {}

    @Override
    public E create(E entity) {
        RequestBody body = RequestBody.create(gson.toJson(entity), JSON_MEDIA_TYPE);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .put(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return entity;
        }
    }

    @Override
    public E findById(ID id) {
        Request request = new Request.Builder()
                .url(url.concat("/").concat(id.toString()))
                .header("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if(response.code() == 200) {
                return gson.fromJson(response.body().string(), entityClass);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Optional<E> findByIdOptional(ID id) {
        E entity = findById(id);
        if(entity == null) {
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public Collection<E> findAll() {
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            E[] array = (E[]) gson.fromJson(response.body().string(), entityClass.arrayType());
            return Arrays.asList(array);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id) != null;
    }

    @Override
    public E save(E entity) {
        return create(entity);
    }

    @Override
    public E delete(E entity) {
        return deleteById(entity.getId());
    }

    @Override
    public E deleteById(ID id) {
        Request request = new Request.Builder()
                .url(url.concat("/").concat(id.toString()))
                .header("Authorization", token)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return gson.fromJson(response.body().string(), entityClass);
        } catch (Exception e) {
            return null;
        }
    }
}
