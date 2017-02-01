package vivatexai.rasp_pi_android_bt.common;

public class DeviceListItem {
    private String name;
    private String address;

    public DeviceListItem(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "- Bluetooth Device -\nname: " + name + "\naddress: " + address;
    }
}
