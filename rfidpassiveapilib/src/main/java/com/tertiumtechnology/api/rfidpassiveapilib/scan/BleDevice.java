package com.tertiumtechnology.api.rfidpassiveapilib.scan;

public class BleDevice {
    private String name;
    private String address;
    private int rssi;

    public BleDevice(String name, String address, int rssi) {
        this.name = name;
        this.address = address;
        this.rssi = rssi;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof BleDevice && address.equals(((BleDevice) o).getAddress());
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getRssi() {
        return rssi;
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }
}