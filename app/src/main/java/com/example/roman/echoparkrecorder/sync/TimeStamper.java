package com.example.roman.echoparkrecorder.sync;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by roman on 7/28/16.
 */
public class TimeStamper {
    private FileNameDispenser mFileNameDispenser;

    public enum StampRecorderType{DATA,AUDIO, VIDEO}

    public TimeStamper(){
        mFileNameDispenser = new FileNameDispenser();
    }

    public void stampCmdMetadata(StampRecorderType stampRecorderType) {
        stampFile(getMetadataFile(stampRecorderType), getCmdTimeStamp());
    }

    public void stampInitMetadata(StampRecorderType stampRecorderType) {
        stampFile(getMetadataFile(stampRecorderType), getInitTimeStamp());
    }
    public void stampStopMetadata(StampRecorderType stampRecorderType) {
        stampFile(getMetadataFile(stampRecorderType), getStopTimeStamp());
    }
    public void stampServerTimeOffsetLog(){
        stampFile(getServerOffsetLogFile(),getServerOffsetTimeStamp());
    }
    private File getMetadataFile(StampRecorderType stampRecorderType) {
        switch ((stampRecorderType)) {
            case DATA: return getDataMetadataFile();
            case AUDIO: return getAudioMetadataFile();
            case VIDEO: return getVideoMetadataFile();
            default:
                LogUtils.d("getMetadataFile ERROR default reached!");
                return null;
        }
    }
    private File getDataMetadataFile(){
        return new File(mFileNameDispenser.getDataFileMetadataPath());
    }
    private File getAudioMetadataFile(){
        return new File(mFileNameDispenser.getAudioFileMetadataPath());
    }
    private File getVideoMetadataFile(){
        return new File(mFileNameDispenser.getVideoFileMetadataPath());
    }
    private File getServerOffsetLogFile(){
        return new File(mFileNameDispenser.getServerTimeOffsetLogFilePath());
    }
    private String getCmdTimeStamp() {
        return "cmdTime:" + String.valueOf(TimeKeeper.getInstance().getTime()) + "\r\n";
    }
    private String getInitTimeStamp(){
        return "initTime:" + String.valueOf(TimeKeeper.getInstance().getTime()) + "\r\n";
    }
    private String getStopTimeStamp(){
        return  "stopTime:" + String.valueOf(TimeKeeper.getInstance().getTime()) + "\r\n";
    }
    private String getServerOffsetTimeStamp(){
        return String.valueOf(TimeKeeper.getInstance().getTimeOffset()) + "\r\n";
    }

    private void stampFile(File file, String timeStamp) {
        LogUtils.d(file.getAbsolutePath());
        try {
            LogUtils.d(timeStamp);
            FileOutputStream outputStream = new FileOutputStream(file,true);
            outputStream.write(timeStamp.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            LogUtils.d("stampMetadata failed");
            e.printStackTrace();
        }
    }


}
