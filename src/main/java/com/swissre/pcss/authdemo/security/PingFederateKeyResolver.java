package com.swissre.pcss.authdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Slf4j
class PingFederateKeyResolver implements SigningKeyResolver {

    private static final String KEY_ID = "kid";

    private final String x509BaseUrl;

    public PingFederateKeyResolver(String x509BaseUrl) {
        this.x509BaseUrl = x509BaseUrl;
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        String keyId = (String) header.get(KEY_ID);
        String pk = getKeyFromServer(keyId);
        return getPublicKey(pk);
    }

    @Override
    public Key resolveSigningKey(JwsHeader header, String plaintext) {
        return resolveSigningKey(header, (Claims) null);
    }

    private String getKeyFromServer(String keyId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        HttpEntity<Void> request = new HttpEntity<Void>(headers);
        String url = this.x509BaseUrl + "?v=" + keyId;
        RestTemplate keyUriRestTemplate = new RestTemplate();
        return (String) keyUriRestTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
    }

    private PublicKey getPublicKey(String x509Certificate) {
        try {
            CertificateFactory f = CertificateFactory.getInstance("X.509");
            InputStream certStream = new ByteArrayInputStream(x509Certificate.getBytes(StandardCharsets.UTF_8));
            X509Certificate certificate = (X509Certificate) f.generateCertificate(certStream);
            return certificate.getPublicKey();
        } catch (CertificateException e) {
            log.warn("Could not create public key from certificate");
            return null;
        }
    }

}

