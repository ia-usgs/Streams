package org.example;

import jakarta.xml.bind.annotation.*;

@XmlRootElement
class EncryptionData {
    private String data;
    private boolean encrypted;

    public EncryptionData() {
    }

    public EncryptionData(String data, boolean encrypted) {
        this.data = data;
        this.encrypted = encrypted;
    }

    @XmlElement
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @XmlElement
    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
}
