package com.hotmart.notifications.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateType {

    DATA_ACCESS("acesso-a-dados"),
    CHANGE_PASSWORD("alterar-senha"),
    MEMBER_AREA_CREATED("area-membros-criada"),
    TWO_FACTOR_AUTHENTICATED("autenticacao-dois-fatores"),
    SECURITY_CODE("codigo-de-seguranca"),
    APPROVED_PURCHASE("compra-aprovada"),
    VALIDATED_DATA("dados-validados"),
    HOTMART_CLUB("hotmart-club"),
    PAYMENT_REMINDER_1("lembrete-de-pagamento-1"),
    PAYMENT_REMINDER_2("lembrete-de-pagamento-2"),
    NOT_DEBITED("nao-debitado"),
    PAYMENT_PIX("pagamento-pix"),
    RECEIVED_ORDER("recebemos-pedido"),
    REFUND("reembolso");

    private final String page;
}
