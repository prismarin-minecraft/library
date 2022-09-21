package in.prismar.library.meta.processor;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface MetaProcessor {



    void process(Class<?> target) throws Exception;
    MetaProcessorType getType();



}
