package tech.zerofiltre.freeland.domain.client.model;

import tech.zerofiltre.freeland.domain.*;
import tech.zerofiltre.freeland.domain.client.*;

import java.util.*;


public class Client {

    private ClientId clientId;
    private String description;
    private String phoneNumber;
    private Address address;

    private ClientProvider clientProvider;


    private Client(ClientBuilder clientBuilder) {
        this.clientId = clientBuilder.clientId;
        this.description = clientBuilder.description;
        this.phoneNumber = clientBuilder.phoneNumber;
        this.address = clientBuilder.address;
        this.clientProvider = clientBuilder.clientProvider;
    }

    public Client register(){
        this.clientId = clientProvider.registerClient(this).getClientId();
        return this;
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    public Optional<Client> of(ClientId clientId) {
        Optional<Client> result = clientProvider.clientOfId(clientId);
        result.ifPresent(client -> client.clientProvider = this.clientProvider);
        return result;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public static class ClientBuilder {
        private ClientId clientId;
        private String description;
        private String phoneNumber;
        private Address address;

        private ClientProvider clientProvider;

        public ClientBuilder clientId(ClientId clientId) {
            this.clientId = clientId;
            return this;
        }

        public ClientBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ClientBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public ClientBuilder address(Address address) {
            this.address = address;
            return this;
        }

        public ClientBuilder clientProvider(ClientProvider clientProvider) {
            this.clientProvider = clientProvider;
            return this;
        }

        public Client build() {
            return new Client(this);
        }

    }


}
