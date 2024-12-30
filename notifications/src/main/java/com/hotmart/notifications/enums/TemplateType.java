package com.hotmart.notifications.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TemplateType {

    DATA_ACCESS("acesso-a-dados", "[Hotmart] Acesso aos Dados"),
    CHANGE_PASSWORD("alterar-senha", "Altere sua senha"),
    MEMBER_AREA_CREATED("area-membros-criada", "Sua área de membros %s foi criada com sucesso!"),
    TWO_FACTOR_AUTHENTICATED("autenticacao-dois-fatores", "Autenticação em dois fatores"),
    SECURITY_CODE("codigo-de-seguranca", "Hotmart | Código de segurança: %s"),
    APPROVED_PURCHASE("compra-aprovada", "Hotmart | Compra aprovada! O acesso ao seu produto chegou!"),
    VALIDATED_DATA("dados-validados", "Hotmart | Boas notícias: seus dados foram validados!"),
    HOTMART_CLUB("hotmart-club", "Seu Acesso ao %s"),
    PAYMENT_REMINDER_1("lembrete-de-pagamento-1", "Conseguiu fazer o pagamento do %s?"),
    PAYMENT_REMINDER_2("lembrete-de-pagamento-2", "Seu pix do %s vai vencer AMANHÃ..."),
    NOT_DEBITED("nao-debitado", "Não foi possível debitar seu Cartão"),
    PAYMENT_PIX("pagamento-pix", "Hotmart | Finalize sua compra"),
    RECEIVED_ORDER("recebemos-pedido", "Hotmart | Recebemos o pedido de %s"),
    REFUND("reembolso", "Hotmart | Seu reembolso foi solicitado!"),
    REFUND_PROCESSED("reembolso-processado", "Hotmart | Seu reembolso foi processado");

    private final String page;
    private final String title;
}
