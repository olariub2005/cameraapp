package com.software.android.entities;

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.software.android.cameraapp.Permission;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Logger;
import com.software.android.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * <ul>
 * <li>3g2 (Mobile Video)</li>
 * <li>3gp (Mobile Video)</li>
 * <li>3gpp (Mobile Video)</li>
 * <li>asf (Windows Media Video)</li>
 * <li>avi (AVI Video)</li>
 * <li>dat (MPEG Video)</li>
 * <li>divx (DIVX Video)</li>
 * <li>dv (DV Video)</li>
 * <li>f4v (Flash Video)</li>
 * <li>flv (Flash Video)</li>
 * <li>m2ts (M2TS Video)</li>
 * <li>m4v (MPEG-4 Video)</li>
 * <li>mkv (Matroska Format)</li>
 * <li>mod (MOD Video)</li>
 * <li>mov (QuickTime Movie)</li>
 * <li>mp4 (MPEG-4 Video)</li>
 * <li>mpe (MPEG Video)</li>
 * <li>mpeg (MPEG Video)</li>
 * <li>mpeg4 (MPEG-4 Video)</li>
 * <li>mpg (MPEG Video)</li>
 * <li>mts (AVCHD Video)</li>
 * <li>nsv (Nullsoft Video)</li>
 * <li>ogm (Ogg Media Format)</li>
 * <li>ogv (Ogg Video Format)</li>
 * <li>qt (QuickTime Movie)</li>
 * <li>tod (TOD Video)</li>
 * <li>ts (MPEG Transport Stream)</li>
 * <li>vob (DVD Video)</li>
 * <li>wmv (Windows Media Video)</li>
 * </ul>
 * The aspect ratio of the video must be between 9x16 and 16x9, and the video
 * cannot exceed 1024MB or 20 minutes in length. <br>
 * <br>
 *
 * 
 * // @see https://developers.facebook.com/docs/reference/api/video
 */
public class Video implements Publishable {

    private static final String COMMENTS = "comments";
    private static final String CREATED_TIME = "created_time";
    private static final String DESCRIPTION = "description";
    private static final String EMBED_HTML = "embed_html";
    private static final String FROM = "from";
    private static final String ICON = "icon";
    private static final String ID = "id";
    private static final String NAME = "name"; // same as NAME
    private static final String PICTURE = "picture";
    private static final String SOURCE = "source";
    private static final String TAGS = "tags";
    private static final String UPDATED_TIME = "updated_time";
    private static final String TITLE = "title"; // same as TITLE
    private static final String PRIVACY = "privacy";

    @SerializedName(COMMENTS)
    private Utils.DataResult<Comment> mComments;

    @SerializedName(CREATED_TIME)
    private Date mCreatedTime = null;

    @SerializedName(DESCRIPTION)
    private String mDescription = null;

    @SerializedName(EMBED_HTML)
    private String mEmbedHtml = null;

    @SerializedName(FROM)
    private User mFrom = null;

    @SerializedName(ICON)
    private String mIcon = null;

    @SerializedName(ID)
    private String mId = null;

    @SerializedName(NAME)
    private String mName = null;

    @SerializedName(PICTURE)
    private String mPicture = null;

    @SerializedName(SOURCE)
    private String mSource = null;

    @SerializedName(TAGS)
    private Utils.DataResult<User> mTags = null;

    @SerializedName(UPDATED_TIME)
    private Date mUpdatedTime = null;

    @SerializedName(PRIVACY)
    private Privacy mPrivacy = null;

    private String mFileName = null;
    private Parcelable mParcelable = null;
    private byte[] mBytes = null;


    private Video(Builder builder) {
        mDescription = builder.mDescription;
        mName = builder.mTitle;
        mFileName = builder.mFileName;
        mPrivacy = builder.mPrivacy;
        mParcelable = builder.mParcelable;
        mBytes = builder.mBytes;
    }

    @Override
    public String getPath() {
        return GraphPath.VIDEOS;
    }

    @Override
    public Permission getPermission() {
        return Permission.PUBLISH_ACTION;
    }

    public Bundle getBundle() {
        Bundle bundle = new Bundle();

        // add title
        if (mName != null) {
            bundle.putString(TITLE, mName);
        }

        // add description
        if (mDescription != null) {
            bundle.putString(DESCRIPTION, mDescription);
        }

        // add privacy
        if (mPrivacy != null) {
            bundle.putString(PRIVACY, mPrivacy.getJSONString());
        }

        // add video
        if (mParcelable != null) {
            bundle.putParcelable(mFileName, mParcelable);
        }
        else if (mBytes != null) {
            bundle.putByteArray(mFileName, mBytes);
        }

        return bundle;
    }

    /**
     * All of the comments on this video
     */
    public List<Comment> getComments() {
        return mComments.data;
    }

    /**
     * The time the video was initially published
     */
    public Date getCreatedTime() {
        return mCreatedTime;
    }

    /**
     * The description of the video
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * The html element that may be embedded in an Web page to play the video
     */
    public String getEmbedHtml() {
        return mEmbedHtml;
    }

    /**
     * The profile (user or page) that created the video
     */
    public User getFrom() {
        return mFrom;
    }

    /**
     * The icon that Facebook displays when video are published to the Feed
     */
    public String getIcon() {
        return mIcon;
    }

    /**
     * The video ID
     */
    public String getId() {
        return mId;
    }

    /**
     * The video title or caption
     */
    public String getName() {
        return mName;
    }

    /**
     * The URL for the thumbnail picture for the video
     */
    public String getPicture() {
        return mPicture;
    }

    /**
     * A URL to the raw, playable video file
     */
    public String getSource() {
        return mSource;
    }

    /**
     * The users who are tagged in this video
     */
    public String getFileName() {
        return mFileName;
    }
    public List<User> getTags() {
        return mTags.data;
    }

    /**
     * The last time the video or its caption were updated
     */
    public Date getUpdateTime() {
        return mUpdatedTime;
    }

    public static class Builder {

        private String mDescription = null;
        private String mTitle = null;
        private String mFileName = null;
        private Privacy mPrivacy = null;

        private Parcelable mParcelable = null;
        private byte[] mBytes = null;

        public Builder() {
        }

        /**
         * Set video to be published.<br>
         * <br>
         * The aspect ratio of the video must be between 9x16 and 16x9, and the
         * video cannot exceed 1024MB or 20 minutes in length.
         *
         * @param file
         */
        public Builder setVideo(File file) {
            try {
                mParcelable = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                mFileName = file.getName();
            }
            catch (FileNotFoundException e) {
                Logger.logError(Photo.class, "Failed to create photo from file", e);
            }
            return this;
        }

        /**
         * Set video to be published.<br>
         * <br>
         * The aspect ratio of the video must be between 9x16 and 16x9, and the
         * video cannot exceed 1024MB or 20 minutes in length.
         *
         * @param bytes
         */
        public Builder setVideo(String fileName, byte[] bytes) {
            mBytes = bytes;
            mFileName = fileName;
            return this;
        }

        /**
         * Set description of the video.
         *
         * @param description
         *            The description of the video
         */
        public Builder setDescription(String description) {
            mDescription = description;
            return this;
        }

        /**
         * Set name of the video.
         *
         * @param title
         *            The name of the video
         */
        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        /**
         * Set privacy settings of the video.
         *
         * @param privacy
         *            The privacy settings of the video
         * @see Privacy
         */
        public Builder setPrivacy(Privacy privacy) {
            mPrivacy = privacy;
            return this;
        }

        public Video build() {
            return new Video(this);
        }
    }

}
