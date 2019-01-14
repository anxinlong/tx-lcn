package com.codingapi.tx.client.spi.transaction.txc.control;

import com.codingapi.tx.client.spi.transaction.txc.resource.def.TxcService;
import com.codingapi.tx.client.support.common.TransactionCleanService;
import com.codingapi.tx.commons.exception.TransactionClearException;
import com.codingapi.tx.commons.exception.TxcLogicException;
import com.codingapi.tx.logger.TxLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Date: 2018/12/13
 *
 * @author ujued
 */
@Component
@Slf4j
public class TxcTransactionCleanService implements TransactionCleanService {

    private final TxcService txcService;

    private final TxLogger txLogger;

    @Autowired
    public TxcTransactionCleanService(TxcService txcService, TxLogger txLogger) {
        this.txcService = txcService;
        this.txLogger = txLogger;
    }

    @Override
    public void clear(String groupId, int state, String unitId, String unitType) throws TransactionClearException {
        try {
            // 若需要回滚读undo_log，进行回滚
            if (state != 1 && state != -1) {
                txcService.undo(groupId, unitId);
            }

            // 清理TXC
            txcService.cleanTxc(groupId, unitId);
        } catch (TxcLogicException e) {
            throw new TransactionClearException(e);
        }
    }
}