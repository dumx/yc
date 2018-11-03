package com.pay.yc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
* 支付配置
* @author dumingxin
* @Date 2017/11/17 15:49
*/
@ConfigurationProperties(prefix = "hub", ignoreUnknownFields = false)
public class HubProperties {

    private final Async async = new Async();

    private final Weixin weixin = new Weixin();

    private final Alipay alipay = new Alipay();

    public Async getAsync() {
        return this.async;
    }

    public Weixin getWeixin() {
        return this.weixin;
    }

    public Alipay getAlipay() {
        return this.alipay;
    }

    public static class Async {

        private int corePoolSize = 2;

        private int maxPoolSize = 50;

        private int queueCapacity = 10000;

        public int getCorePoolSize() {
            return this.corePoolSize;
        }

        public void setCorePoolSize(final int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return this.maxPoolSize;
        }

        public void setMaxPoolSize(final int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getQueueCapacity() {
            return this.queueCapacity;
        }

        public void setQueueCapacity(final int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }
    }

    public static class Weixin {

        private String appid;

        private String mch_id;

        private String secret;

        private String package_value;

        private String notify_url;

        private String local_cert_url;

        public String getAppid() {
            return this.appid;
        }

        public void setAppid(final String appid) {
            this.appid = appid;
        }

        public String getMch_id() {
            return this.mch_id;
        }

        public void setMch_id(final String mch_id) {
            this.mch_id = mch_id;
        }


        public String getSecret() {
            return this.secret;
        }

        public void setSecret(final String secret) {
            this.secret = secret;
        }

        public String getPackage_value() {
            return this.package_value;
        }

        public void setPackage_value(final String package_value) {
            this.package_value = package_value;
        }

        public String getNotify_url() {
            return this.notify_url;
        }

        public void setNotify_url(final String notify_url) {
            this.notify_url = notify_url;
        }

        public String getLocal_cert_url() {
            return this.local_cert_url;
        }

        public void setLocal_cert_url(final String local_cert_url) {
            this.local_cert_url = local_cert_url;
        }
    }


    public static class Alipay {
        private String ali_appid="2017111609968522";

        private String partner;

        private String private_key="MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCW8SfQH+IQ32BlVQpRl3do3TWuFJzKOg3tRwPbXa+r3yTnuc2jsrrIIh+ai2z9KStb6EoBwPTmKIdOLPUiypGA9EmthGAubmE157ng34tvASKAM81OabKCHs8yjb+waj7YOrmE2L4wtLGKuYCbUbrGJm2/F6/+J7AXJBY9Pj71du970bB0xj9QKgavwrCAcua4OWYhmrS9tJX97cP1Y34XetwU6dqBxrHpTr5jHS8sbVy2LTDuCcm8Ll/UwPzorWyRIcYVTVd8USCY+cjl7e8rCYhN68gzYwfUTER5YQXuaLZL4rgbnJUsetEkKHgxGh5EgIOF+B2N505+bJbhMYkFAgMBAAECggEAeMxc4XpysLAkdFRforbGkLzgdgX3CPz3IlsAIjJTIgi+f1k60vR5+SseAc/tlJm7Ip2/hLHJDN2Ik671LDfTxNE8mcpkMySRVdOy9d3RuC+/TbynZUa/3tULW1kH/Qa6btBoooHXjCFAKVaWLzd0c5pToGgzqAJZMdvmlHLZbhXbK/irY8XHX65Y9X0p/dyVeyODpT/FfsW1vLG4IEQioq/NqG+jE0Am7Ox/ok2rgt83xt6Lsut2XvthV5MIqhJt95U4XE7S+AVHiz28Ba7Io6UMyBCztLAg4Nr/xOyzjuRe7xipzNSVmuB4bSd1FT/MV0k6f0YTq0f6gxlg0B5jDQKBgQDPry+hR8sjaREKE8RYsCHRaIJnjBk24bI7Fj/LCCFa++idbsn4HbdXM5xG056xKJRZNANp9+Mss4R764ERQ0SvKJzB3AzAHinfiObS0Mvrt+lczEg2dUHDlpUOCuQvkPrEcFh8qXxtE8jytU6MUhFbjJAA/bZ+qbU//pi3uI8j2wKBgQC6DqI/dKqT6a+UdLiQGHu+7NUA5NLlplekWy4ChdSDCxTSlrvIn/3otdisGE9eJRBTfepLA0tdi57I2fm1BeoamY/x92SbODevfIgqcXJmnk8MmYsbcQ+TRzCua3+LPpjTGH15VWeCQ7mHzF2X1nCBv34Nl6miNCvb3nlscJQMnwKBgErgUj/HbomZu7byYXvIJKknELzcWcrbnDKJVv6QM9Zls/vukYqOzCb4SIOQkEo24yU2B3URFiprnnw+8A2VPxIXhkyuVTp+pYIeDXo8JQkbCVuXmwFnTQqDI8IoUxSDJKXmC0EGqgF5fJ1kmKkmfXOQPerdjgokjWBBHjY+p/y5AoGBALHmnCygmRmlMEV69Z6SSoBcY67fHOiJDPQSWwP+gxxp1BcoGZXTpSyF9A9Zj0otlla+5u+izsU7YzAZfDIi3Z1hh8mNC51/O3+IS4qJvyFv7ZXWmrQ6fvIL9/hOwT2bNPVjTVZe2wIrT5MRTcyJNjpZp2XHxmNaDzXEm8WcP3NxAoGAO+JdoNTO52tISe3dGhogBojQOLScNUXtDcaIXWQaw1pjc1AvVGFwHFVu+Vv7XcZqPKDYo5O31SRMxDoY4F2LgmpYZX8WCfrrYwyuKXpeytXb20yxmeZVQk2kyaruvFSA6LKiQEanARWPkKxIxWu8ClA8Xz42PUX5yWYZST97VL4=";

        private String public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlvEn0B/iEN9gZVUKUZd3aN01rhScyjoN7UcD212vq98k57nNo7K6yCIfmots/SkrW+hKAcD05iiHTiz1IsqRgPRJrYRgLm5hNee54N+LbwEigDPNTmmygh7PMo2/sGo+2Dq5hNi+MLSxirmAm1G6xiZtvxev/iewFyQWPT4+9Xbve9GwdMY/UCoGr8KwgHLmuDlmIZq0vbSV/e3D9WN+F3rcFOnagcax6U6+Yx0vLG1cti0w7gnJvC5f1MD86K1skSHGFU1XfFEgmPnI5e3vKwmITevIM2MH1ExEeWEF7mi2S+K4G5yVLHrRJCh4MRoeRICDhfgdjedOfmyW4TGJBQIDAQAB";

        private String alipay_public_key="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo/YTNZU7VWQm20xvPdDwMDzhLWdUipSPt2Fd9XQfp3mu8bB7oHBgd31RI3zN7n5y+omuUpqtaguxqc7LJo/DhnLZynR49IkD2nbkuX5zn2h8jNFegSALoLwU2fJc6dnjSxDw9PoORSfHQJXLdQbwlwQYHk6nzr75bs++Fjx/PhgQRY3/8Qhq5+l9LS4aW7i9UwWNy7paL6axMqkinR+3sHtjZWrlemIskqiObvdJ+eQu1KbFopOvT5Xs76dKh1k/A8vfu6lEVcmRi1YDtHUhn0Sp3Nobw1vJh6Z+JpYiaBDleHeuioY8ZNT9SpXVXao4J9zQRkMuBLNLdZ1okJ5+ywIDAQAB";

        private String sign_type="RSA2";

        private String log_path;

        private String input_charset="utf-8";

        private String format="JSON";

        private String version="1.0";

        private String method="alipay.trade.app.pay";

        private String notify_url="http://116.62.12.100:8899/w/notifies/alipay";

        private String seller_id;

        private String  refund_server;

        public String getAli_appid() {
            return this.ali_appid;
        }

        public void setAli_appid(final String ali_appid) {
            this.ali_appid = ali_appid;
        }

        public String getPartner() {
            return this.partner;
        }

        public void setPartner(final String partner) {
            this.partner = partner;
        }

        public String getPrivate_key() {
            return this.private_key;
        }

        public void setPrivate_key(final String private_key) {
            this.private_key = private_key;
        }

        public String getPublic_key() {
            return this.public_key;
        }

        public void setPublic_key(final String public_key) {
            this.public_key = public_key;
        }


        public String getAlipay_public_key() {
            return this.alipay_public_key;
        }

        public void setAlipay_public_key(final String alipay_public_key) {
            this.alipay_public_key = alipay_public_key;
        }

        public String getSign_type() {
            return this.sign_type;
        }

        public void setSign_type(final String sign_type) {
            this.sign_type = sign_type;
        }

        public String getLog_path() {
            return this.log_path;
        }

        public void setLog_path(final String log_path) {
            this.log_path = log_path;
        }

        public String getInput_charset() {
            return this.input_charset;
        }

        public void setInput_charset(final String input_charset) {
            this.input_charset = input_charset;
        }

        public String getFormat() {
            return this.format;
        }

        public void setFormat(final String format) {
            this.format = format;
        }

        public String getVersion() {
            return this.version;
        }

        public void setVersion(final String version) {
            this.version = version;
        }

        public String getMethod() {
            return this.method;
        }

        public void setMethod(final String method) {
            this.method = method;
        }

        public String getNotify_url() {
            return this.notify_url;
        }

        public void setNotify_url(final String notify_url) {
            this.notify_url = notify_url;
        }

        public String getSeller_id() {
            return this.seller_id;
        }

        public void setSeller_id(final String seller_id) {
            this.seller_id = seller_id;
        }

        public String getRefund_server() {
            return this.refund_server;
        }

        public void setRefund_server(final String refund_server) {
            this.refund_server = refund_server;
        }


    }
}
