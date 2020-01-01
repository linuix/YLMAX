package com.xbl.ylmax.entity;

/**
 * 设备配置信息
 */
public class DeviceInfo {
    public String status;
    public Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }


    public String getVideoUrl(){
        return data.tok_1.video.url;
    }

    public String getVideoTitle(){
        return data.tok_1.video.title;
    }

    static class Video{
        public String title;
        public String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "Video{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }



    }

    static class TokDY{
        public Video video;
        public String talking;
        public String follow;

        public Video getVideo() {
            return video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public String getTalking() {
            return talking;
        }

        public void setTalking(String talking) {
            this.talking = talking;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }

        @Override
        public String toString() {
            return "TokDY{" +
                    "video='" + video + '\'' +
                    ", talking='" + talking + '\'' +
                    ", follow='" + follow + '\'' +
                    '}';
        }
    }


    static class DuoSan{
        public String talking;
        public String follow;

        public String getTalking() {
            return talking;
        }

        public void setTalking(String talking) {
            this.talking = talking;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }

        @Override
        public String toString() {
            return "DuoSan{" +
                    "talking='" + talking + '\'' +
                    ", follow='" + follow + '\'' +
                    '}';
        }
    }

    static class Data{
        public TokDY tok_1;
        public TokDY tok_2;
        public DuoSan san_1;
        public DuoSan san_2;

        public int follow_num_1;
        public int follow_num_2;
        public int fans_num_1;
        public int fans_num_2;
        public int private_num_1;
        public int private_num_2;

        public TokDY getTok_1() {
            return tok_1;
        }

        public void setTok_1(TokDY tok_1) {
            this.tok_1 = tok_1;
        }

        public TokDY getTok_2() {
            return tok_2;
        }

        public void setTok_2(TokDY tok_2) {
            this.tok_2 = tok_2;
        }

        public DuoSan getSan_1() {
            return san_1;
        }

        public void setSan_1(DuoSan san_1) {
            this.san_1 = san_1;
        }

        public DuoSan getSan_2() {
            return san_2;
        }

        public void setSan_2(DuoSan san_2) {
            this.san_2 = san_2;
        }

        public int getFollow_num_1() {
            return follow_num_1;
        }

        public void setFollow_num_1(int follow_num_1) {
            this.follow_num_1 = follow_num_1;
        }

        public int getFollow_num_2() {
            return follow_num_2;
        }

        public void setFollow_num_2(int follow_num_2) {
            this.follow_num_2 = follow_num_2;
        }

        public int getFans_num_1() {
            return fans_num_1;
        }

        public void setFans_num_1(int fans_num_1) {
            this.fans_num_1 = fans_num_1;
        }

        public int getFans_num_2() {
            return fans_num_2;
        }

        public void setFans_num_2(int fans_num_2) {
            this.fans_num_2 = fans_num_2;
        }

        public int getPrivate_num_1() {
            return private_num_1;
        }

        public void setPrivate_num_1(int private_num_1) {
            this.private_num_1 = private_num_1;
        }

        public int getPrivate_num_2() {
            return private_num_2;
        }

        public void setPrivate_num_2(int private_num_2) {
            this.private_num_2 = private_num_2;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "tok_1=" + tok_1 +
                    ", tok_2=" + tok_2 +
                    ", san_1=" + san_1 +
                    ", san_2=" + san_2 +
                    ", follow_num_1=" + follow_num_1 +
                    ", follow_num_2=" + follow_num_2 +
                    ", fans_num_1=" + fans_num_1 +
                    ", fans_num_2=" + fans_num_2 +
                    ", private_num_1=" + private_num_1 +
                    ", private_num_2=" + private_num_2 +
                    '}';
        }
    }
}






