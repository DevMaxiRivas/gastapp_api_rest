package com.app.listener;

import com.app.event.transaction.TransactionCreatedEvent;
import com.app.event.transaction.TransactionDeletedEvent;
import com.app.event.transaction.TransactionUpdatedEvent;
import com.app.service.SSE.SseNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionServiceListener {
    private final SseNotificationService sseNotificationService;

    @Async
    @EventListener
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        sseNotificationService.notify(
                event.userId(),
                "transaction-created",
                "New transaction saved"
        );
    }

    @Async
    @EventListener
    public void handleTransactionUpdated(TransactionUpdatedEvent event) {
        sseNotificationService.notify(
                event.userId(),
                "transaction-updated",
                "New transaction updated"
        );
    }

    @Async
    @EventListener
    public void handleTransactionDeleted(TransactionDeletedEvent event) {
        sseNotificationService.notify(
                event.userId(),
                "transaction-deleted",
                "New transaction deleted"
        );
    }
}
