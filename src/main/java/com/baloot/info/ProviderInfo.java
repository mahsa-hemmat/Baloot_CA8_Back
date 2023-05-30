package com.baloot.info;

import com.baloot.model.*;

import java.util.StringTokenizer;

public class ProviderInfo {
    private final int id;
    private final String name;
    private final String registryDate;
    private final String image;

    public ProviderInfo(Provider provider) {
        id = provider.getId();
        name = provider.getName();
        StringTokenizer st = new StringTokenizer(provider.getRegistryDate(), "-");
        registryDate = st.nextToken();
        image = provider.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public String getImage() {
        return image;
    }
}