package com.oceanebelle.samples.obzkclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZkManagerImpl implements ZkManager {
    private static ZooKeeper zkeeper;
    private static ZkConnection zkConnection;

    public ZkManagerImpl() throws IOException, InterruptedException {
        initialize();
    }

    private void initialize() throws IOException, InterruptedException {
        zkConnection = new ZkConnection();
        zkeeper = zkConnection.connect("localhost");
    }

    public void closeConnection() throws InterruptedException {
        zkConnection.close();
    }

    public void create(String path, byte[] data) throws KeeperException, InterruptedException {

        zkeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public Object getZNodeData(String path, boolean watchFlag)
            throws KeeperException, InterruptedException, UnsupportedEncodingException {
  
        byte[] b = null;
        b = zkeeper.getData(path, null, null);
        return new String(b, "UTF-8");
    }
 
    public void update(String path, byte[] data) throws KeeperException, 
      InterruptedException {
        int version = zkeeper.exists(path, true).getVersion();
        zkeeper.setData(path, data, version);
    }
}