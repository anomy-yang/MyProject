package app;

import util.Util;

import java.io.File;
import java.util.Objects;

public class FileMeta {

    private String name;
    private String path;
    private Long size;//防止有默认值
    private Long lastModified;
    private Boolean isDirectory;
    private String sizeText;
    private String lastModifiedText;

    //传入参数，要有特定的值，以免出现空值

    public FileMeta(File child){
       this(child.getName(),
                child.getParent(),child.length(),child.lastModified(),child.isDirectory());
    }


    public FileMeta(String name, String path, Long size, Long lastModified, Boolean isDirectory) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.isDirectory = isDirectory;

        this.sizeText = Util.parseSize(size);
        this.lastModifiedText = Util.parseData(lastModified);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta fileMeta = (FileMeta) o;
        return Objects.equals(name, fileMeta.name) &&
                Objects.equals(path, fileMeta.path) &&
                Objects.equals(isDirectory, fileMeta.isDirectory);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, path, isDirectory);
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isDirectory=" + isDirectory +
                ", sizeText='" + sizeText + '\'' +
                ", lastModifiedText='" + lastModifiedText + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }
}
