package br.com.selat.appsample.config;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.*;

@Configuration
public class AwsIotMqttConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AwsIotMqttConfig.class);

    @Bean
    public AWSIotMqttClient awsIotMqttClient() throws AWSIotException {

        String awsIotClientEndpoint = Optional.ofNullable(System.getenv("AWS_IOT_CLIENT_ENDPOINT")).orElse("");
        String awsIotClientId =  Optional.ofNullable(System.getenv("AWS_IOT_CLIENT_ID")).orElse("appSample");
        String certificatesPath =  Optional.ofNullable(System.getenv("AWS_IOT_CERTIFICATES_PATH")).orElse("certs");

        awsIotClientId = awsIotClientId + UUID.randomUUID();

        KeyStorePasswordPair pair = getKeyStorePasswordPair(certificatesPath+"/cert.pem", certificatesPath+"/key.pem", "RSA");
        if (pair == null){
            LOGGER.error("Null keystore");
            return null;
        }
        AWSIotMqttClient mqttClient = new AWSIotMqttClient(awsIotClientEndpoint, awsIotClientId, pair.getKeyStore(), pair.getKeyPassword());
        mqttClient.setConnectionTimeout(1000);
        mqttClient.setMaxConnectionRetries(3);
        mqttClient.setKeepAliveInterval(30000);
        mqttClient.setCleanSession(false);
        mqttClient.connect();
        return mqttClient;
    }

    public static class KeyStorePasswordPair {
        private final KeyStore keyStore;
        private final String keyPassword;

        KeyStorePasswordPair(KeyStore keyStore, String keyPassword) {
            this.keyStore = keyStore;
            this.keyPassword = keyPassword;
        }

        public KeyStore getKeyStore() {
            return keyStore;
        }

        public String getKeyPassword() {
            return keyPassword;
        }

    }

    public static KeyStorePasswordPair getKeyStorePasswordPair(final String certificateFile, final String privateKeyFile,
                                                               String keyAlgorithm) {
        if (certificateFile == null || privateKeyFile == null) {
            LOGGER.error("Certificate or private key file missing");
            return null;
        }
        LOGGER.info("Cert file: {} Private key: {}", certificateFile, privateKeyFile);

        final PrivateKey privateKey = loadPrivateKeyFromFile(privateKeyFile, keyAlgorithm);

        final Collection<? extends Certificate> certChain = loadCertificatesFromFile(certificateFile);

        if (certChain.isEmpty() || privateKey == null) return null;

        return getKeyStorePasswordPair(certChain, privateKey);
    }

    private static KeyStorePasswordPair getKeyStorePasswordPair(final Collection<? extends Certificate> certificates, final PrivateKey privateKey) {
        KeyStore keyStore;
        String keyPassword;
        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            // randomly generated key password for the key in the KeyStore
            keyPassword = new BigInteger(128, new SecureRandom()).toString(32);

            Certificate[] certChain = new Certificate[certificates.size()];
            certChain = certificates.toArray(certChain);
            keyStore.setKeyEntry("alias", privateKey, keyPassword.toCharArray(), certChain);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            LOGGER.error("Failed to create key store");
            return null;
        }

        return new KeyStorePasswordPair(keyStore, keyPassword);
    }

    private static Collection<? extends Certificate> loadCertificatesFromFile(final String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            LOGGER.error("Certificate file: {} not found", filename);
            return Collections.emptyList();
        }

        try (BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file))) {
            final CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            return certFactory.generateCertificates(stream);
        } catch (IOException | CertificateException e) {
            LOGGER.error("Failed to load certificate file {}", filename, e);
        }
        return Collections.emptyList();
    }

    private static PrivateKey loadPrivateKeyFromFile(final String filename, final String algorithm) {
        PrivateKey privateKey = null;

        File file = new File(filename);
        if (!file.exists()) {
            LOGGER.error("Private key file not found: {}", filename);
            return null;
        }
        try (DataInputStream stream = new DataInputStream(new FileInputStream(file))) {
            privateKey = PrivateKeyReader.getPrivateKey(stream, algorithm);
        } catch (IOException | GeneralSecurityException e) {
            LOGGER.error("Failed to load private key from file {}", filename);
        }

        return privateKey;
    }
}
