package com.appointment.user_service.Oauth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleVerifier {

    private final NetHttpTransport transport = new NetHttpTransport();
    private final JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;

    public GoogleUser verify(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                return new GoogleUser(
                        (String) payload.get("email"),
                        (String) payload.get("name")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public record GoogleUser(String email, String name) {}
}
