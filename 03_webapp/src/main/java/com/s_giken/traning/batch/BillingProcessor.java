package com.s_giken.traning.batch;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import com.s_giken.traning.batch.model.Billing;
import com.s_giken.traning.webapp.model.MemberCharge;
import lombok.NonNull;

public class BillingProcessor implements ItemProcessor<MemberCharge, Billing> {

    private static final Logger log = LoggerFactory.getLogger(BillingProcessor.class);

    @Override
    @NonNull
    public Billing process(final MemberCharge memberCharge) throws Exception {
        final Billing billing = new Billing();
        billing.setCharge(memberCharge.getCharge().getAmount());
        log.info("");
        return billing;
    }
}
