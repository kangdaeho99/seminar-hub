package com.seminarhub.batch.delivery.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

@Slf4j
public class DeliveryChunkListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("▶▶▶ 청크 시작 (트랜잭션 시작 직후)");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("◀◀◀ 청크 종료 (트랜잭션 커밋 직전)");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.error("청크 에러 발생");
    }
}
