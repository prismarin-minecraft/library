package in.prismar.library.common.repository.impl;

import in.prismar.library.common.delayed.DelayedOperation;
import in.prismar.library.common.delayed.DelayedOperationExecutor;
import in.prismar.library.common.repository.AsyncRepository;
import in.prismar.library.common.repository.Repository;
import lombok.Getter;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class AbstractAsyncRepository<ID, E> implements Repository<ID, E>, AsyncRepository<ID, E> {

    private final DelayedOperationExecutor<DelayedOperation> executor;

    private final long delayedExecutorDelay;

    public AbstractAsyncRepository(String delayedExecutorName, long delayedExecutorDelay) {
        this.executor = new DelayedOperationExecutor<>(delayedExecutorName);
        this.delayedExecutorDelay = delayedExecutorDelay;
    }

    @Override
    public CompletableFuture<E> createAsync(E entity) {
        return CompletableFuture.supplyAsync(() -> create(entity), executor.getThreadPool());
    }


    @Override
    public CompletableFuture<E> findByIdAsync(ID id) {
        return CompletableFuture.supplyAsync(() -> findById(id), executor.getThreadPool());
    }


    @Override
    public CompletableFuture<Optional<E>> findByIdOptionalAsync(ID id) {
        return CompletableFuture.supplyAsync(() -> findByIdOptional(id), executor.getThreadPool());
    }

    @Override
    public CompletableFuture<Collection<E>> findAllAsync() {
        return CompletableFuture.supplyAsync(() -> findAll(), executor.getThreadPool());
    }

    @Override
    public CompletableFuture<Boolean> existsByIdAsync(ID id) {
        return CompletableFuture.supplyAsync(() -> existsById(id), executor.getThreadPool());
    }

    @Override
    public CompletableFuture<E> saveAsync(E entity, boolean delayed) {
        if(!delayed) {
            return CompletableFuture.supplyAsync(() -> save(entity), executor.getThreadPool());
        }
        CompletableFuture<E> future = new CompletableFuture<>();
        this.executor.addTask(entity.toString(), delayedExecutorDelay, () -> {
            future.complete(save(entity));
        });
        return future;
    }

    @Override
    public CompletableFuture<E> deleteAsync(E entity) {
        if(this.executor.hasTask(entity.toString())) {
            this.executor.removeTask(entity.toString());
        }
        return CompletableFuture.supplyAsync(() -> delete(entity), executor.getThreadPool());
    }

    @Override
    public CompletableFuture<E> deleteByIdAsync(ID id) {
        if(this.executor.hasTask(id.toString())) {
            this.executor.removeTask(id.toString());
        }
        return CompletableFuture.supplyAsync(() -> deleteById(id), executor.getThreadPool());
    }
}
