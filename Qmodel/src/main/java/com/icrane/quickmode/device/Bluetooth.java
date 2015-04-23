package com.icrane.quickmode.device;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import com.icrane.quickmode.app.QModel;
import com.icrane.quickmode.app.Releasable;
import com.icrane.quickmode.app.activity.QModelActivity;
import com.icrane.quickmode.utils.common.CommonUtils;
import com.icrane.quickmode.utils.reflect.Reflector;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by gujiwen on 15/4/7.
 */
public final class Bluetooth implements Releasable {

    private static Bluetooth mBluetooth = null;

    private Set<BroadcastReceiver> broadcastReceiverSet = Collections.
            synchronizedSet(new LinkedHashSet<BroadcastReceiver>());
    private Activity context;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mRemoteDevice;

    private UUIDs uuids = UUIDs.SERIAL_PORT_SERVICE_CLASS_UUID;
    private UUID uuid;
    private String name;
    private String address;

    private Bluetooth() {
        this.context = QModel.getTopActivity();
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.name = mBluetoothAdapter.getName();
        this.address = mBluetoothAdapter.getAddress();
        this.uuid = uuids.uuid();
    }

    public static Bluetooth getInstance() {
        if (CommonUtils.isEmpty(mBluetooth)) {
            mBluetooth = new Bluetooth();
        }
        return mBluetooth;
    }

    /**
     * 设备是否支持蓝牙
     *
     * @return 返回boolean值，如果为true表示设备支持蓝牙，false则不支持
     */
    public boolean isSupport() {
        if (mBluetoothAdapter == null) {
            QModelActivity.ToastAsist.showShortToast(this.context, "Your device does not support Bluetooth!");
            return false;
        }
        return true;
    }

    /**
     * 注册广播
     *
     * @param receiver 只接收蓝牙发出的广播的广播接收器
     */
    public void registerBluetoothBroadcast(BroadcastReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_LOCAL_NAME_CHANGED);
        this.context.registerReceiver(receiver, intentFilter);
        this.broadcastReceiverSet.add(receiver);
    }

    /**
     * 启动蓝牙
     *
     * @param requestCode 方法内部调用了startActivityForResult()所以必须带以一个请求码。
     */
    public void enable(int requestCode) {
        if (isSupport()) {
            //设备是否启动了蓝牙
            if (!mBluetoothAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                this.context.startActivityForResult(intent, requestCode);
            }
        }
    }

    /**
     * 使蓝牙能被其他设备检测到，如果蓝牙没有打开，会在用户选择了Yes后，启动蓝牙，并在指定时间内被其他设备发现。
     *
     * @param duration    这是要设置的时间，时间在0~3600秒内，设置为0表示无论何时都可被其他设备检测到
     * @param requestCode 方法内部调用了startActivityForResult()所以必须带以一个请求码。
     */
    public void discoverable(int duration, int requestCode) {
        if (isSupport()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
            this.context.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取已经匹配的蓝牙设备
     *
     * @return 已经匹配的蓝牙设备
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    /**
     * 获取所有已经监听的广播接收者
     *
     * @return 所有已经监听的广播接收者
     */
    public Set<BroadcastReceiver> getBroadcastReceiverSet() {
        return broadcastReceiverSet;
    }

    /**
     * 开始扫描设备
     */
    public void startDiscovery() {
        if (mBluetoothAdapter.isDiscovering()) {
            cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * 关闭扫描
     */
    public void cancelDiscovery() {
        mBluetoothAdapter.cancelDiscovery();
    }

    /**
     * 创建配对
     *
     * @param device 蓝牙设备
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void createBond(BluetoothDevice device) {
        device.createBond();
    }

    /**
     * 取消配对进程
     *
     * @param device 蓝牙设备
     */
    public void cancelBondProcess(BluetoothDevice device) {
        try {
            Reflector.invokeMethod(device,
                    Reflector.getMethod(device.getClass(),
                            "cancelBondProcess", Reflector.ReflectType.DEFAULT));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消用户输入
     *
     * @param device 蓝牙设备
     */
    public void cancelPairingUserInput(BluetoothDevice device) {
        try {
            Reflector.invokeMethod(device, Reflector.getMethod(
                    device.getClass(), "cancelPairingUserInput",
                    Reflector.ReflectType.DEFAULT));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除配对
     *
     * @param device 蓝牙设备
     */
    public void removeBond(BluetoothDevice device) {
        try {
            Reflector.invokeMethod(device, Reflector.getMethod(
                    device.getClass(), "removeBond", Reflector.ReflectType.DEFAULT));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前你的设备
     *
     * @return 当前你的设备
     */
    public BluetoothDevice getRemoteDevice() {
        if (BluetoothAdapter.checkBluetoothAddress(getAddress())) {
            mRemoteDevice = mBluetoothAdapter.getRemoteDevice(getAddress());
        }
        return mRemoteDevice;
    }

    /**
     * 获取蓝牙适配器
     *
     * @return 蓝牙适配器
     */
    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    /**
     * 设置UUID
     *
     * @param uuids uuid
     */
    public void setUUID(UUIDs uuids) {
        this.uuid = uuids.uuid();
    }

    /**
     * 获取唯一的UUID
     *
     * @return uuid
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * 设置蓝牙服务名称
     *
     * @param name 蓝牙服务名称
     */
    public void setName(String name) {
        mBluetoothAdapter.setName(name);
    }

    /**
     * 获取蓝牙服务名称
     *
     * @return 蓝牙服务名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取蓝牙设备地址
     *
     * @return 蓝牙设备地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        if (mBluetooth != null) {
            mBluetooth.cancelDiscovery();
            Set<BroadcastReceiver> broadcastReceivers = mBluetooth.getBroadcastReceiverSet();
            if (broadcastReceivers.size() > 0) {
                for (BroadcastReceiver receiver : broadcastReceivers) {
                    this.context.unregisterReceiver(receiver);
                }
            }
            mBluetooth = null;
        }
    }

    /**
     * 蓝牙接收器，负责作为服务端时，接收外部的BluetoothSocket连接
     */
    public static class BluetoothAcceptor extends Thread {

        public boolean isAccept = false;
        private final BluetoothServerSocket mBluetoothServerSocket;
        private OnManagerAcceptedSocket listener;

        public BluetoothAcceptor(OnManagerAcceptedSocket listener) {

            BluetoothServerSocket bluetoothServerSocket = null;
            this.listener = listener;
            try {
                bluetoothServerSocket = mBluetooth.getBluetoothAdapter()
                        .listenUsingRfcommWithServiceRecord(mBluetooth.getName(), mBluetooth.getUUID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.mBluetoothServerSocket = bluetoothServerSocket;

        }

        @Override
        public void run() {
            BluetoothSocket socket = null;
            while (!isAccept) {
                try {
                    socket = mBluetoothServerSocket.accept();
                    if (socket != null) {
                        isAccept = true;
                        if (listener != null) {
                            listener.managerAcceptedSocket(socket);
                            mBluetoothServerSocket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 关闭ServerSocket
         */
        public void cancel() {
            try {
                mBluetoothServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 蓝牙连接者，可对指定设备发送一个连接
     */
    public static class BluetoothConnector extends Thread {

        private BluetoothDevice device;
        private BluetoothSocket socket;
        private OnManagerConnectedSocket listener;

        public BluetoothConnector(BluetoothDevice device, OnManagerConnectedSocket listener) {
            this.device = device;
            this.listener = listener;
            BluetoothSocket bSocket = null;
            try {
                bSocket = this.device.createRfcommSocketToServiceRecord(mBluetooth.getUUID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.socket = bSocket;
        }

        @Override
        public void run() {
            mBluetooth.getBluetoothAdapter().cancelDiscovery();
            try {
                this.socket.connect();
            } catch (IOException e) {
                try {
                    this.socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (listener != null) {
                listener.managerConnectedSocket(this.socket);
            }
        }
    }

    /**
     * 管理BluetoothSocket回调接口
     */
    public interface OnManagerConnectedSocket {
        public void managerConnectedSocket(BluetoothSocket socket);
    }

    /**
     * 管理BluetoothSocket回调接口
     */
    public interface OnManagerAcceptedSocket {
        public void managerAcceptedSocket(BluetoothSocket socket);
    }

}
