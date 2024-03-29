package com.software.android.cameraapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.software.android.actions.ConnectDeviceAction;
import com.software.android.actions.DeleteRequestAction;
import com.software.android.actions.GetAccountsAction;
import com.software.android.actions.GetAction;
import com.software.android.actions.GetAlbumAction;
import com.software.android.actions.GetAlbumsAction;
import com.software.android.actions.GetAppRequestsAction;
import com.software.android.actions.GetAttachmentAction;
import com.software.android.actions.GetCommentAction;
import com.software.android.actions.GetCommentsAction;
import com.software.android.actions.GetEventsAction;
import com.software.android.actions.GetFamilyAction;
import com.software.android.actions.GetFriendsAction;
import com.software.android.actions.GetGroupsAction;
import com.software.android.actions.GetInvitableFriendsAction;
import com.software.android.actions.GetLikesAction;
import com.software.android.actions.GetNotificationsAction;
import com.software.android.actions.GetPageAction;
import com.software.android.actions.GetPagesAction;
import com.software.android.actions.GetPhotoAction;
import com.software.android.actions.GetPhotosAction;
import com.software.android.actions.GetPostsAction;
import com.software.android.actions.GetProfileAction;
import com.software.android.actions.GetScoresAction;
import com.software.android.actions.GetStoryObjectsAction;
import com.software.android.actions.GetTaggableFriendsAction;
import com.software.android.actions.GetTaggedPlacesAction;
import com.software.android.actions.GetVideosAction;
import com.software.android.actions.InviteAction;
import com.software.android.actions.PollDeviceAuthorizationAction;
import com.software.android.actions.PublishAction;
import com.software.android.actions.PublishFeedDialogAction;
import com.software.android.actions.PublishPhotoDialogAction;
import com.software.android.actions.PublishStoryDialogAction;
import com.software.android.entities.Album;
import com.software.android.entities.Checkin;
import com.software.android.entities.Comment;
import com.software.android.entities.Event;
import com.software.android.entities.Event.EventDecision;
import com.software.android.entities.Feed;
import com.software.android.entities.Group;
import com.software.android.entities.Like;
import com.software.android.entities.Page;
import com.software.android.entities.Photo;
import com.software.android.entities.Post;
import com.software.android.entities.Post.PostType;
import com.software.android.entities.Profile;
import com.software.android.entities.Profile.Properties;
import com.software.android.entities.Publishable;
import com.software.android.entities.Score;
import com.software.android.entities.Story;
import com.software.android.entities.Story.StoryObject;
import com.software.android.entities.Video;
import com.software.android.listeners.OnAccountsListener;
import com.software.android.listeners.OnActionListener;
import com.software.android.listeners.OnAlbumListener;
import com.software.android.listeners.OnAlbumsListener;
import com.software.android.listeners.OnAppRequestsListener;
import com.software.android.listeners.OnAttachmentListener;
import com.software.android.listeners.OnAuthorizationDeviceListener;
import com.software.android.listeners.OnCommentListener;
import com.software.android.listeners.OnCommentsListener;
import com.software.android.listeners.OnConnectDeviceListener;
import com.software.android.listeners.OnCreateStoryObject;
import com.software.android.listeners.OnDeleteListener;
import com.software.android.listeners.OnEventsListener;
import com.software.android.listeners.OnFamilyListener;
import com.software.android.listeners.OnFriendsListener;
import com.software.android.listeners.OnGroupsListener;
import com.software.android.listeners.OnInviteListener;
import com.software.android.listeners.OnLikesListener;
import com.software.android.listeners.OnLoginListener;
import com.software.android.listeners.OnLogoutListener;
import com.software.android.listeners.OnNewPermissionsListener;
import com.software.android.listeners.OnNotificationsListener;
import com.software.android.listeners.OnPageListener;
import com.software.android.listeners.OnPagesListener;
import com.software.android.listeners.OnPhotoListener;
import com.software.android.listeners.OnPhotosListener;
import com.software.android.listeners.OnPostsListener;
import com.software.android.listeners.OnProfileListener;
import com.software.android.listeners.OnPublishListener;
import com.software.android.listeners.OnScoresListener;
import com.software.android.listeners.OnStoryObjectsListener;
import com.software.android.listeners.OnTaggedPlacesListener;
import com.software.android.listeners.OnVideosListener;
import com.software.android.utils.GraphPath;
import com.software.android.utils.Utils;

import java.util.List;
import java.util.Set;

/**
 * Simple Facebook SDK which wraps original Facebook SDK
 *
 * 
 */
public class SimpleFacebook {

    private static SimpleFacebook mInstance = null;
    private static SimpleFacebookConfiguration mConfiguration = new SimpleFacebookConfiguration.Builder().build();

    private static SessionManager mSessionManager = null;

    private SimpleFacebook() {
    }

    /**
     * Initialize the library and pass an {@link android.app.Activity}. This kind of
     * initialization is good in case you have a one base activity and many
     * fragments. In this case you just initialize this library and then just
     * get an instance of this library by {@link com.software.android.cameraapp.SimpleFacebook#getInstance()}
     * in any other place.
     *
     * @param activity
     *            Activity
     */
    public static void initialize(Activity activity) {
        if (mInstance == null) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            mInstance = new SimpleFacebook();
            mSessionManager = new SessionManager(mConfiguration);
        }
        mSessionManager.setActivity(activity);
    }

    /**
     * Get the instance of {@link com.software.android.cameraapp.SimpleFacebook}. This method, not only returns
     * a singleton instance of {@link com.software.android.cameraapp.SimpleFacebook} but also updates the
     * current activity with the passed activity. <br>
     * If you have more than one <code>Activity</code> in your application. And
     * more than one activity do something with facebook. Then, call this method
     * in {@link android.app.Activity#onResume()} method
     *
     * <pre>
     * &#064;Override
     * protected void onResume() {
     * 	super.onResume();
     * 	mSimpleFacebook = SimpleFacebook.getInstance(this);
     * }
     * </pre>
     *
     * @param activity
     * @return {@link com.software.android.cameraapp.SimpleFacebook} instance
     */
    public static SimpleFacebook getInstance(Activity activity) {
        if (mInstance == null) {
            FacebookSdk.sdkInitialize(activity.getApplicationContext());
            mInstance = new SimpleFacebook();
            mSessionManager = new SessionManager(mConfiguration);
        }
        mSessionManager.setActivity(activity);
        return mInstance;
    }

    /**
     * Get the instance of {@link com.software.android.cameraapp.SimpleFacebook}. <br>
     * <br>
     * <b>Important:</b> Use this method only after you initialized this library
     * or by: {@link #initialize(android.app.Activity)} or by {@link #getInstance(android.app.Activity)}
     *
     * @return The {@link com.software.android.cameraapp.SimpleFacebook} instance
     */
    public static SimpleFacebook getInstance() {
        return mInstance;
    }

    /**
     * Set facebook configuration. <b>Make sure</b> to set a configuration
     * before first actual use of this library like (login, getProfile, etc..).
     *
     * @param configuration
     *            The configuration of this library
     */
    public static void setConfiguration(SimpleFacebookConfiguration configuration) {
        mConfiguration = configuration;
        SessionManager.configuration = configuration;
    }

    /**
     * Get configuration
     *
     * @return
     */
    public static SimpleFacebookConfiguration getConfiguration() {
        return mConfiguration;
    }

    /**
     * Login to Facebook
     *
     * @param onLoginListener
     */
    public void login(OnLoginListener onLoginListener) {
        mSessionManager.login(onLoginListener);
    }

    /**
     * Logout from Facebook
     */
    public void logout(OnLogoutListener onLogoutListener) {
        mSessionManager.logout(onLogoutListener);
    }

    /**
     * Are we logged in to facebook
     *
     * @return <code>True</code> if we have active and open session to facebook
     */
    public boolean isLogin() {
        return mSessionManager.isLogin();
    }

    /**
     * General GET method.
     *
     * @param entityId
     *            The id of the entity you want to retrieve.
     * @param edge
     *            The graph edge. Like "friends", "groups" ...
     * @param bundle
     *            The 'get' parameters
     * @param onActionListener
     *            The listener with the type you expect as response.
     */
    public <T> void get(String entityId, String edge, final Bundle bundle, OnActionListener<T> onActionListener) {
        GetAction<T> getAction = new GetAction<T>(mSessionManager) {
            @Override
            protected Bundle getBundle() {
                if (bundle != null) {
                    return bundle;
                }
                return super.getBundle();
            }
        };
        getAction.setActionListener(onActionListener);
        getAction.setTarget(entityId);
        getAction.setEdge(edge);
        getAction.execute();
    }

    /**
     * The pages of which the current user is an admin.
     *
     * @param onAccountsListener
     */
    public void getAccounts(OnAccountsListener onAccountsListener) {
        GetAccountsAction getAccountsAction = new GetAccountsAction(mSessionManager);
        getAccountsAction.setActionListener(onAccountsListener);
        getAccountsAction.execute();
    }

    /**
     * Get album by album id.
     *
     * @param albumId
     *            The album id.
     * @param onAlbumListener
     *            The callback listener.
     */
    public void getAlbum(String albumId, OnAlbumListener onAlbumListener) {
        GetAlbumAction getAlbumAction = new GetAlbumAction(mSessionManager);
        getAlbumAction.setActionListener(onAlbumListener);
        getAlbumAction.setTarget(albumId);
        getAlbumAction.execute();
    }

    /**
     * Get my albums.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_PHOTOS}
     *
     * @param onAlbumsListener
     *            The callback listener
     */
    public void getAlbums(OnAlbumsListener onAlbumsListener) {
        GetAlbumsAction getAlbumsAction = new GetAlbumsAction(mSessionManager);
        getAlbumsAction.setActionListener(onAlbumsListener);
        getAlbumsAction.execute();
    }

    /**
     * Get albums of entity. <br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Profile</b>. It can be you, your friend or any other profile. To get
     * id of the profile: {@link Profile#getId()}<br>
     * - <b>Page</b>. It can be any page. To get the page id:
     * {@link Page#getId()}<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_PHOTOS}<br>
     *
     * @param entityId
     *            profile id or page id.
     * @param onAlbumsListener
     *            The callback listener.
     */
    public void getAlbums(String entityId, OnAlbumsListener onAlbumsListener) {
        GetAlbumsAction getAlbumsAction = new GetAlbumsAction(mSessionManager);
        getAlbumsAction.setActionListener(onAlbumsListener);
        getAlbumsAction.setTarget(entityId);
        getAlbumsAction.execute();
    }

    /**
     * Get all app requests made by me to others or by others to me.
     *
     * @param onAppRequestsListener
     *            The callback listener.
     */
    public void getAppRequests(OnAppRequestsListener onAppRequestsListener) {
        GetAppRequestsAction getAppRequestsAction = new GetAppRequestsAction(mSessionManager);
        getAppRequestsAction.setActionListener(onAppRequestsListener);
        getAppRequestsAction.execute();
    }

    /**
     * Get attachment of specific entity.
     *
     * @param onAttachmentListener
     *            The callback listener.
     */
    public void getAttachment(String entityId, OnAttachmentListener onAttachmentListener) {
        GetAttachmentAction getAttachmentAction = new GetAttachmentAction(mSessionManager);
        getAttachmentAction.setActionListener(onAttachmentListener);
        getAttachmentAction.setTarget(entityId);
        getAttachmentAction.execute();
    }

    /**
     * Get my books. The response as you can notice is a Page because everything
     * in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getBooks(OnPagesListener onPagesListener) {
        getBooks(null, null, onPagesListener);
    }

    /**
     * Get my books and set the properties you need. The response as you can
     * notice is a Page because everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getBooks(Page.Properties properties, OnPagesListener onPagesListener) {
        getBooks(null, properties, onPagesListener);
    }

    /**
     * Get books of entity. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getBooks(String entityId, OnPagesListener onPagesListener) {
        getBooks(entityId, null, onPagesListener);
    }

    /**
     * Get books of entity and set properties that you need. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getBooks(String entityId, Page.Properties properties, OnPagesListener onPagesListener) {
        GetPagesAction getPagesAction = new GetPagesAction(mSessionManager);
        getPagesAction.setActionListener(onPagesListener);
        getPagesAction.setProperties(properties);
        getPagesAction.setTarget(entityId);
        getPagesAction.setEdge(GraphPath.BOOKS);
        getPagesAction.execute();
    }

    /**
     * Get comment by comment id.
     *
     * @param commentId
     *            The comment id.
     * @param onCommentListener
     *            The callback listener.
     */
    public void getComment(String commentId, OnCommentListener onCommentListener) {
        GetCommentAction getCommentAction = new GetCommentAction(mSessionManager);
        getCommentAction.setActionListener(onCommentListener);
        getCommentAction.setTarget(commentId);
        getCommentAction.execute();
    }

    /**
     * Get comments of specific entity.<br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Album</b>. Any album. To get the album id: {@link Album#getId()}<br>
     * - <b>Checkin</b>. Any checkin. To get the checkin id:
     * {@link Checkin#getId()}<br>
     * - <b>Comment</b>. Get all replied comments to this original comment. To
     * get comment id: {@link Comment#getId()} <br>
     * - <b>Photo</b>. Any photo. To get the photo id: {@link Photo#getId()} <br>
     * - <b>Post</b>. Any post. To get the post id: {@link Post#getId()} <br>
     * - <b>Video</b>. Any video. To get the video id: {@link Video#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permission is needed, except the permission you asked for
     * getting the entity itself. For example, if you want to get comments of
     * album, you need to have the {@link com.software.android.cameraapp.Permission#USER_PHOTOS} for getting
     * the comments of this album.
     *
     * @param entityId
     *            Album, Checkin, Comment, Link, Photo, Post or Video.
     * @param onCommentsListener
     *            The callback listener.
     */
    public void getComments(String entityId, OnCommentsListener onCommentsListener) {
        GetCommentsAction getCommentsAction = new GetCommentsAction(mSessionManager);
        getCommentsAction.setActionListener(onCommentsListener);
        getCommentsAction.setTarget(entityId);
        getCommentsAction.execute();
    }

    /**
     * Get events of the user. Select which events you want to get by passing
     * EventDesicion.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_EVENTS}<br>
     *
     * @param eventDecision
     *            The type of event: attending, maybe, declined.
     * @param onEventsListener
     *            The callback listener.
     */
    public void getEvents(EventDecision eventDecision, OnEventsListener onEventsListener) {
        GetEventsAction getEventsAction = new GetEventsAction(mSessionManager);
        getEventsAction.setActionListener(onEventsListener);
        getEventsAction.setEventDecision(eventDecision);
        getEventsAction.execute();
    }

    /**
     * Get events of specific entity.<br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Profile</b>. It can be you, your friend or any other profile. To get
     * id of the profile: {@link Profile#getId()}<br>
     * - <b>Page</b>. Any page. To get the page id: {@link Page#getId()}<br>
     * - <b>Group</b>. Any group. To get the group id: {@link Group#getId()}<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_EVENTS}<br>
     *
     * @param entityId
     *            Profile, Page or Group.
     * @param eventDecision
     *            The type of event: attending, maybe, declined.
     * @param onEventsListener
     *            The callback listener.
     */
    public void getEvents(String entityId, EventDecision eventDecision, OnEventsListener onEventsListener) {
        GetEventsAction getEventsAction = new GetEventsAction(mSessionManager);
        getEventsAction.setActionListener(onEventsListener);
        getEventsAction.setEventDecision(eventDecision);
        getEventsAction.setTarget(entityId);
        getEventsAction.execute();
    }

    /**
     * Get person's family relationships.<br>
     * <br>
     * The result will be id + name of other user and the relationship between
     * you two. For example: wife, brother, mother,.. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_RELATIONSHIPS}<br>
     *
     * @param onFamilyListener
     */
    public void getFamily(OnFamilyListener onFamilyListener) {
        GetFamilyAction getFamilyAction = new GetFamilyAction(mSessionManager);
        getFamilyAction.setActionListener(onFamilyListener);
        getFamilyAction.execute();
    }

    /**
     * Get family relationships of entity.<br>
     * <br>
     * The entity can be only:<br>
     * - <b>Profile</b>. It can be you, your friend or any other profile. To get
     * id of the profile: {@link Profile#getId()}<br>
     * <br>
     *
     * The result will be id + name of other user and the relationship between
     * you two. For example: wife, brother, mother,.. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_RELATIONSHIPS}<br>
     *
     * @param entityId
     * @param onFamilyListener
     */
    public void getFamily(String entityId, OnFamilyListener onFamilyListener) {
        GetFamilyAction getFamilyAction = new GetFamilyAction(mSessionManager);
        getFamilyAction.setActionListener(onFamilyListener);
        getFamilyAction.setTarget(entityId);
        getFamilyAction.execute();
    }

    /**
     * Get person's friends from facebook.<br>
     * This method will return profile with next default properties depends on
     * permissions you have: <b><em>id, name</em></b><br>
     * <br>
     *
     * @param onFriendsListener
     *            The callback listener.
     */
    public void getFriends(OnFriendsListener onFriendsListener) {
        getFriends(null, onFriendsListener);
    }

    /**
     * Get my friends from facebook by mentioning specific parameters. <br>
     * For example, if you need: <em>id, last_name, picture, birthday</em>
     *
     * @param onFriendsListener
     *            The callback listener.
     * @param properties
     *            The {@link Properties}. <br>
     *            To create {@link Properties} instance use:
     *
     *            <pre>
     * // define the friend picture we want to get
     * PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
     * pictureAttributes.setType(PictureType.SQUARE);
     * pictureAttributes.setHeight(500);
     * pictureAttributes.setWidth(500);
     *
     * // create properties
     * Properties properties = new Properties.Builder().add(Properties.ID).add(Properties.LAST_NAME).add(Properties.PICTURE, attributes).add(Properties.BIRTHDAY).build();
     * </pre>
     */
    public void getFriends(Properties properties, OnFriendsListener onFriendsListener) {
        GetFriendsAction getFriendsAction = new GetFriendsAction(mSessionManager);
        getFriendsAction.setProperties(properties);
        getFriendsAction.setActionListener(onFriendsListener);
        getFriendsAction.execute();
    }

    public void getTaggableFriends(OnFriendsListener onFriendsListener) {
        getTaggableFriends(null, onFriendsListener);
    }

    public void getTaggableFriends(Properties properties, OnFriendsListener onFriendsListener) {
        GetFriendsAction getFriendsAction = new GetTaggableFriendsAction(mSessionManager);
        getFriendsAction.setProperties(properties);
        getFriendsAction.setActionListener(onFriendsListener);
        getFriendsAction.execute();
    }

    public void getInvitableFriends(OnFriendsListener onFriendsListener) {
        getInvitableFriends(null, onFriendsListener);
    }

    public void getInvitableFriends(Properties properties, OnFriendsListener onFriendsListener) {
        GetFriendsAction getFriendsAction = new GetInvitableFriendsAction(mSessionManager);
        getFriendsAction.setProperties(properties);
        getFriendsAction.setActionListener(onFriendsListener);
        getFriendsAction.execute();
    }

    public void getTaggedPlaces(OnTaggedPlacesListener onTaggedPlacesListener) {
        GetTaggedPlacesAction getTaggedPlacesAction = new GetTaggedPlacesAction(mSessionManager);
        getTaggedPlacesAction.setActionListener(onTaggedPlacesListener);
        getTaggedPlacesAction.execute();
    }

    /**
     * Get my games. The response as you can notice is a Page because everything
     * in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getGames(OnPagesListener onPagesListener) {
        getGames(null, null, onPagesListener);
    }

    /**
     * Get my games and set the properties you need. The response as you can
     * notice is a Page because everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getGames(Page.Properties properties, OnPagesListener onPagesListener) {
        getGames(null, properties, onPagesListener);
    }

    /**
     * Get games of entity. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getGames(String entityId, OnPagesListener onPagesListener) {
        getGames(entityId, null, onPagesListener);
    }

    /**
     * Get games that entity like and set properties that you need. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getGames(String entityId, Page.Properties properties, OnPagesListener onPagesListener) {
        GetPagesAction getPagesAction = new GetPagesAction(mSessionManager);
        getPagesAction.setActionListener(onPagesListener);
        getPagesAction.setProperties(properties);
        getPagesAction.setTarget(entityId);
        getPagesAction.setEdge(GraphPath.GAMES);
        getPagesAction.execute();
    }

    /**
     * Get my groups.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_GROUPS}
     *
     * @param onGroupsListener
     *            The callback listener.
     */
    public void getGroups(OnGroupsListener onGroupsListener) {
        GetGroupsAction getGroupsAction = new GetGroupsAction(mSessionManager);
        getGroupsAction.setActionListener(onGroupsListener);
        getGroupsAction.execute();
    }

    /**
     * Get groups that user belongs to.<br>
     * <br>
     *
     * The entity can be:<br>
     * - <b>Profile</b>. It can be you, your friend or any other profile. To get
     * id of the profile: {@link Profile#getId()}<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_GROUPS}<br>
     *
     * @param entityId
     *            Profile
     * @param onGroupsListener
     *            The callback listener.
     */
    public void getGroups(String entityId, OnGroupsListener onGroupsListener) {
        GetGroupsAction getGroupsAction = new GetGroupsAction(mSessionManager);
        getGroupsAction.setActionListener(onGroupsListener);
        getGroupsAction.setTarget(entityId);
        getGroupsAction.execute();
    }

    /**
     * Get likes of specific entity.<br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Album</b>. Any album. To get the album id: {@link Album#getId()}<br>
     * - <b>Checkin</b>. Any checkin. To get the checkin id:
     * {@link Checkin#getId()}<br>
     * - <b>Comment</b>. Get all likes of the comment. To get comment id:
     * {@link Comment#getId()} <br>
     * - <b>Photo</b>. Any photo. To get the photo id: {@link Photo#getId()} <br>
     * - <b>Post</b>. Any post. To get the post id: {@link Post#getId()} <br>
     * - <b>Video</b>. Any video. To get the video id: {@link Video#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permission is needed, except the permission you asked for
     * getting the entity itself. For example, if you want to get likes of
     * album, you need to have the {@link com.software.android.cameraapp.Permission#USER_PHOTOS} for getting
     * likes of this album.
     *
     * @param entityId
     *            Album, Checkin, Comment, Link, Photo, Post or Video.
     * @param onLikesListener
     *            The callback listener.
     */
    public void getLikes(String entityId, OnLikesListener onLikesListener) {
        GetLikesAction getLikesAction = new GetLikesAction(mSessionManager);
        getLikesAction.setActionListener(onLikesListener);
        getLikesAction.setTarget(entityId);
        getLikesAction.execute();
    }

    /**
     * Get my movies. The response as you can notice is a Page because
     * everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMovies(OnPagesListener onPagesListener) {
        getMovies(null, null, onPagesListener);
    }

    /**
     * Get my movies and set the properties you need. The response as you can
     * notice is a Page because everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMovies(Page.Properties properties, OnPagesListener onPagesListener) {
        getMovies(null, properties, onPagesListener);
    }

    /**
     * Get movies of entity. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMovies(String entityId, OnPagesListener onPagesListener) {
        getMovies(entityId, null, onPagesListener);
    }

    /**
     * Get movies that entity like and set properties that you need. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMovies(String entityId, Page.Properties properties, OnPagesListener onPagesListener) {
        GetPagesAction getPagesAction = new GetPagesAction(mSessionManager);
        getPagesAction.setActionListener(onPagesListener);
        getPagesAction.setProperties(properties);
        getPagesAction.setTarget(entityId);
        getPagesAction.setEdge(GraphPath.MOVIES);
        getPagesAction.execute();
    }

    /**
     * Get my music. The response as you can notice is a Page because everything
     * in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMusic(OnPagesListener onPagesListener) {
        getMusic(null, null, onPagesListener);
    }

    /**
     * Get my music and set the properties you need. The response as you can
     * notice is a Page because everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMusic(Page.Properties properties, OnPagesListener onPagesListener) {
        getMusic(null, properties, onPagesListener);
    }

    /**
     * Get music of entity. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMusic(String entityId, OnPagesListener onPagesListener) {
        getMusic(entityId, null, onPagesListener);
    }

    /**
     * Get musics that entity like and set properties that you need. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getMusic(String entityId, Page.Properties properties, OnPagesListener onPagesListener) {
        GetPagesAction getPageAction = new GetPagesAction(mSessionManager);
        getPageAction.setActionListener(onPagesListener);
        getPageAction.setProperties(properties);
        getPageAction.setTarget(entityId);
        getPageAction.setEdge(GraphPath.MUSIC);
        getPageAction.execute();
    }

    /**
     * Get unread Facebook notifications that a person has.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#MANAGE_NOTIFICATIONS}<br>
     *
     * @param onNotificationsListener
     *            The callback listener
     */
    public void getNotifications(OnNotificationsListener onNotificationsListener) {
        GetNotificationsAction getNotificationsAction = new GetNotificationsAction(mSessionManager);
        getNotificationsAction.setActionListener(onNotificationsListener);
        getNotificationsAction.execute();
    }

    /**
     * Get page by page id.
     *
     * @param entityId
     *            The page id.
     * @param onPageListener
     *            The callback listener.
     */
    public void getPage(String entityId, OnPageListener onPageListener) {
        GetPageAction getPageAction = new GetPageAction(mSessionManager);
        getPageAction.setActionListener(onPageListener);
        getPageAction.setTarget(entityId);
        getPageAction.execute();
    }

    /**
     * Get page by page id.
     *
     * @param entityId
     *            The page id.
     * @param properties
     *            Properties you want to get.
     * @param onPageListener
     *            The callback listener.
     */
    public void getPage(String entityId, Page.Properties properties, OnPageListener onPageListener) {
        GetPageAction getPageAction = new GetPageAction(mSessionManager);
        getPageAction.setActionListener(onPageListener);
        getPageAction.setTarget(entityId);
        getPageAction.setProperties(properties);
        getPageAction.execute();
    }

    /**
     * Get pages that user liked
     *
     * @param onPagesListener
     *            The callback listener.
     */
    public void getPages(OnPagesListener onPagesListener) {
        GetPagesAction getPagesAction = new GetPagesAction(mSessionManager);
        getPagesAction.setActionListener(onPagesListener);
        getPagesAction.setEdge(GraphPath.LIKES);
        getPagesAction.execute();
    }

    /**
     * Get an individual photo.
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_PHOTOS}
     *
     * @param entityId
     *            Photo-id.
     * @param onPhotoListener
     *            The callback listener.
     */
    public void getPhoto(String entityId, OnPhotoListener onPhotoListener) {
        GetPhotoAction getPhotoAction = new GetPhotoAction(mSessionManager);
        getPhotoAction.setActionListener(onPhotoListener);
        getPhotoAction.setTarget(entityId);
        getPhotoAction.execute();
    }

    /**
     * Get my photos.
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_PHOTOS}
     *
     * @param onPhotosListener
     *            The callback listener.
     */
    public void getPhotos(OnPhotosListener onPhotosListener) {
        GetPhotosAction getPhotosAction = new GetPhotosAction(mSessionManager);
        getPhotosAction.setActionListener(onPhotosListener);
        getPhotosAction.execute();
    }

    /**
     * Get photos of specific entity.<br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Album</b>. Any album. To get the album id: {@link Album#getId()}<br>
     * - <b>Event</b>. Any event. To get the event id: {@link Event#getId()}<br>
     * - <b>Page</b>. Any page. To get page id: {@link Page#getId()} <br>
     * - <b>Profile</b>. Any profile. To get profile id: {@link Profile#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_PHOTOS}<br>
     *
     * @param entityId
     *            Album, Event, Page, Profile
     * @param onPhotosListener
     *            The callback listener.
     */
    public void getPhotos(String entityId, OnPhotosListener onPhotosListener) {
        GetPhotosAction getPhotosAction = new GetPhotosAction(mSessionManager);
        getPhotosAction.setActionListener(onPhotosListener);
        getPhotosAction.setTarget(entityId);
        getPhotosAction.execute();
    }

    /**
     * Get my profile from facebook.<br>
     * This method will return profile with next default properties depends on
     * permissions you have:<br>
     * <em>id, name, first_name, middle_name, last_name, gender, locale, languages, link, username, timezone, updated_time, verified, bio, birthday, education, email,
     * hometown, location, political, favorite_athletes, favorite_teams, quotes, relationship_status, religion, website, work</em>
     *
     * @param onProfileListener
     *            The callback listener.
     */
    public void getProfile(OnProfileListener onProfileListener) {
        getProfile(null, null, onProfileListener);
    }

    /**
     * Get profile by profile id. <br>
     * The default values only will be returned. For more options, see
     * {@link #getProfile(String, Properties, OnProfileListener)}
     *
     * @param profileId
     *            The id of the profile we want to get
     * @param onProfileListener
     *            The callback listener.
     */
    public void getProfile(String profileId, OnProfileListener onProfileListener) {
        getProfile(profileId, null, onProfileListener);
    }

    /**
     * Get my profile from facebook by mentioning specific parameters. <br>
     * For example, if you need: <em>square picture 500x500 pixels</em>
     *
     * @param onProfileListener
     *            The callback listener.
     * @param properties
     *            The {@link Properties}. <br>
     *            To create {@link Properties} instance use:
     *
     *            <pre>
     * // define the profile picture we want to get
     * PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
     * pictureAttributes.setType(PictureType.SQUARE);
     * pictureAttributes.setHeight(500);
     * pictureAttributes.setWidth(500);
     *
     * // create properties
     * Properties properties = new Properties.Builder().add(Properties.ID).add(Properties.FIRST_NAME).add(Properties.PICTURE, attributes).build();
     * </pre>
     */
    public void getProfile(Profile.Properties properties, OnProfileListener onProfileListener) {
        getProfile(null, properties, onProfileListener);
    }

    /**
     * Get profile by profile id and mentioning specific parameters. <br>
     *
     * @param profileId
     *            The id of the profile we want to get
     * @param onProfileListener
     *            The callback listener.
     * @param properties
     *            The {@link Properties}. <br>
     *            To create {@link Properties} instance use:
     *
     *            <pre>
     * // define the profile picture we want to get
     * PictureAttributes pictureAttributes = Attributes.createPictureAttributes();
     * pictureAttributes.setType(PictureType.SQUARE);
     * pictureAttributes.setHeight(500);
     * pictureAttributes.setWidth(500);
     *
     * // create properties
     * Properties properties = new Properties.Builder().add(Properties.ID).add(Properties.FIRST_NAME).add(Properties.PICTURE, attributes).build();
     * </pre>
     */
    public void getProfile(String profileId, Profile.Properties properties, OnProfileListener onProfileListener) {
        GetProfileAction getProfileAction = new GetProfileAction(mSessionManager);
        getProfileAction.setProperties(properties);
        getProfileAction.setTarget(profileId);
        getProfileAction.setActionListener(onProfileListener);
        getProfileAction.execute();
    }

    /**
     * Get all my feeds on the wall. It includes: links, statuses, photos..
     * everything that appears on my wall.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permissions are needed for getting the public posts. If you
     * want to get more private posts, then you need
     * {@link com.software.android.cameraapp.Permission#READ_STREAM}
     *
     * @param onPostsListener
     *            The callback listener.
     */
    public void getPosts(OnPostsListener onPostsListener) {
        GetPostsAction getPostsAction = new GetPostsAction(mSessionManager);
        getPostsAction.setActionListener(onPostsListener);
        getPostsAction.execute();
    }

    /**
     * Get my posts filtered by {@link PostType}.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permissions are needed for getting the public posts. If you
     * want to get more private posts, then you need
     * {@link com.software.android.cameraapp.Permission#READ_STREAM}<br>
     * <br>
     *
     * @param postType
     *            Filter all wall feeds and get posts that you need.
     * @param onPostsListener
     *            The callback listener.
     */
    public void getPosts(PostType postType, OnPostsListener onPostsListener) {
        GetPostsAction getPostsAction = new GetPostsAction(mSessionManager);
        getPostsAction.setActionListener(onPostsListener);
        getPostsAction.setPostType(postType);
        getPostsAction.execute();
    }

    /**
     * Get all feeds on the wall of specific entity. It includes: links,
     * statuses, photos.. everything that appears on that wall.<br>
     *
     * <br>
     * The entity can be one of:<br>
     * - <b>Group</b>. Any group. To get the group id: {@link Group#getId()}<br>
     * - <b>Event</b>. Any event. To get the event id: {@link Event#getId()}<br>
     * - <b>Page</b>. Any page. To get page id: {@link Page#getId()} <br>
     * - <b>Profile</b>. Any profile. To get profile id: {@link Profile#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permissions are needed for getting the public posts. If you
     * want to get more private posts, then you need
     * {@link com.software.android.cameraapp.Permission#READ_STREAM}
     *
     * @param entityId
     *            Event, Group, Page, Profile
     * @param onPostsListener
     *            The callback listener.
     */
    public void getPosts(String entityId, OnPostsListener onPostsListener) {
        getPosts(entityId, PostType.ALL, onPostsListener);
    }

    /**
     * Get posts of specific entity filtered by {@link PostType}.<br>
     *
     * <br>
     * In case of:
     *
     * <pre>
     * {@link PostType#ALL}
     * </pre>
     *
     * the entity can be one of:<br>
     * - <b>Group</b>. Any group. To get the group id: {@link Group#getId()}<br>
     * - <b>Event</b>. Any event. To get the event id: {@link Event#getId()}<br>
     * - <b>Page</b>. Any page. To get page id: {@link Page#getId()} <br>
     * - <b>Profile</b>. Any profile. To get profile id: {@link Profile#getId()} <br>
     * <br>
     * ---------<br>
     * In case of:
     *
     * <pre>
     * 	{@link PostType#LINKS}
     * 	{@link PostType#POSTS}
     * 	{@link PostType#STATUSES}
     * 	{@link PostType#TAGGED}
     * </pre>
     *
     * the entity can be one of:<br>
     * - <b>Page</b>. Any page. To get page id: {@link Page#getId()} <br>
     * - <b>Profile</b>. Any profile. To get profile id: {@link Profile#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * No special permissions are needed for getting the public posts. If you
     * want to get more private posts, then you need
     * {@link com.software.android.cameraapp.Permission#READ_STREAM}<br>
     * <br>
     *
     * @param entityId
     *            Event, Group, Page, Profile
     * @param postType
     *            Filter all wall feeds and get posts that you need.
     * @param onPostsListener
     *            The callback listener.
     */
    public void getPosts(String entityId, PostType postType, OnPostsListener onPostsListener) {
        GetPostsAction getPostsAction = new GetPostsAction(mSessionManager);
        getPostsAction.setActionListener(onPostsListener);
        getPostsAction.setTarget(entityId);
        getPostsAction.setPostType(postType);
        getPostsAction.execute();
    }

    /**
     *
     * Gets scores using Scores API for games. <br>
     * <br>
     *
     * @param onScoresListener
     *            The callback listener.
     */
    public void getScores(OnScoresListener onScoresListener) {
        GetScoresAction getScoresAction = new GetScoresAction(mSessionManager);
        getScoresAction.setActionListener(onScoresListener);
        getScoresAction.execute();
    }

    /**
     * Get open graph objects that are stored on facebook side.
     *
     * @param objectName
     * @param onStoryObjectsListener
     */
    public void getStoryObjects(String objectName, OnStoryObjectsListener onStoryObjectsListener) {
        GetStoryObjectsAction getStoryObjectsAction = new GetStoryObjectsAction(mSessionManager);
        getStoryObjectsAction.setObjectName(objectName);
        getStoryObjectsAction.setActionListener(onStoryObjectsListener);
        getStoryObjectsAction.execute();
    }

    /**
     * Get my TV shows. The response as you can notice is a Page because
     * everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getTelevision(OnPagesListener onPagesListener) {
        getTelevision(null, null, onPagesListener);
    }

    /**
     * Get my TV shows and set the properties you need. The response as you can
     * notice is a Page because everything in facebook has the model of Page.<br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes'
     * permission is needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getTelevision(Page.Properties properties, OnPagesListener onPagesListener) {
        getTelevision(null, properties, onPagesListener);
    }

    /**
     * Get TV shows of entity. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getTelevision(String entityId, OnPagesListener onPagesListener) {
        getTelevision(entityId, null, onPagesListener);
    }

    /**
     * Get TV shows of entity and set properties that you need. <br>
     * <br>
     *
     * <b>Note:</b><br>
     * In most cases this information is public and thus can be retrieved
     * without permissions, but if user added privacy, then 'user_likes' or/and
     * 'friends_likes' permissions are needed. <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_LIKES}<br>
     */
    public void getTelevision(String entityId, Page.Properties properties, OnPagesListener onPagesListener) {
        GetPagesAction getPagesAction = new GetPagesAction(mSessionManager);
        getPagesAction.setActionListener(onPagesListener);
        getPagesAction.setProperties(properties);
        getPagesAction.setTarget(entityId);
        getPagesAction.setEdge(GraphPath.TELEVISION);
        getPagesAction.execute();
    }

    /**
     * Get my videos.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_VIDEOS}
     *
     * @param onVideosListener
     *            The callback listener.
     */
    public void getVideos(OnVideosListener onVideosListener) {
        GetVideosAction getVideosAction = new GetVideosAction(mSessionManager);
        getVideosAction.setActionListener(onVideosListener);
        getVideosAction.execute();
    }

    /**
     * Get videos of specific entity.<br>
     * <br>
     *
     * The entity can be one of:<br>
     * - <b>Event</b>. Any event. To get the event id: {@link Event#getId()}<br>
     * - <b>Page</b>. Any page. To get page id: {@link Page#getId()} <br>
     * - <b>Profile</b>. Any profile. To get profile id: {@link Profile#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#USER_VIDEOS}<br>
     *
     * @param entityId
     *            Profile, Page, Event
     * @param onVideosListener
     *            The callback listener.
     */
    public void getVideos(String entityId, OnVideosListener onVideosListener) {
        GetVideosAction getVideosAction = new GetVideosAction(mSessionManager);
        getVideosAction.setActionListener(onVideosListener);
        getVideosAction.setTarget(entityId);
        getVideosAction.execute();
    }

    /**
     * Publish comment
     *
     * @param entityId
     * @param comment
     * @param onPublishListener
     */
    public void publish(String entityId, Comment comment, OnPublishListener onPublishListener) {
        publish(entityId, (Publishable) comment, onPublishListener);
    }

    /**
     * Publish like
     *
     * @param entityId
     * @param like
     * @param onPublishListener
     */
    public void publish(String entityId, Like like, OnPublishListener onPublishListener) {
        publish(entityId, (Publishable) like, onPublishListener);
    }

    /**
     *
     * Posts a score using Scores API for games. If missing publish_actions
     * permission, we do not ask again for it.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}
     *
     *
     * @param score
     *            Score to be posted. <code>int</code>
     * @param onPublishListener
     *            The listener for posting score
     */
    public void publish(Score score, OnPublishListener onPublishListener) {
        publish("me", (Publishable) score, onPublishListener);
    }

    /**
     *
     * Publish {@link Feed} on the wall of entity.<br>
     * <br>
     * The entity can be one of:<br>
     * - <b>Group</b>. Any public group. To get the group id:
     * {@link Group#getId()}<br>
     * - <b>Event</b>. Any public event. To get the event id:
     * {@link Event#getId()}<br>
     * - <b>Page</b>. Any page that allowed publishing on their timeline. To get
     * page id: {@link Page#getId()} <br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}
     *
     * <br>
     * <br>
     * <b>Notes:</b><br>
     * - Publishing on friend's wall is <b>no more</b> possible. This option was
     * disabled by Facebook.<br>
     * - If the user is admin of page. And you try to publish a feed on this
     * page. Then you will have to ask for {@link com.software.android.cameraapp.Permission#MANAGE_PAGES}
     * permission in addition to {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}.<br>
     *
     * @param entityId
     *            Group, Event, Page
     * @param feed
     *            The feed to publish. Use {@link Feed.Builder} to create a new
     *            <code>Feed</code>
     * @param onPublishListener
     *            The listener for publishing action
     */
    public void publish(String entityId, Feed feed, OnPublishListener onPublishListener) {
        publish(entityId, (Publishable) feed, onPublishListener);
    }

    /**
     *
     * Publish {@link Feed} on the wall.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}
     *
     * @param feed
     *            The feed to publish. Use {@link Feed.Builder} to create a new
     *            <code>Feed</code>
     * @param onPublishListener
     *            The listener for publishing action
     */
    public void publish(Feed feed, OnPublishListener onPublishListener) {
        publish("me", (Publishable) feed, onPublishListener);
    }

    /**
     * Share to feed by using dialog or do it silently without dialog. <br>
     * If you use dialog for sharing, you don't have to configure
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION} since user does the share by himself.<br>
     * <br>
     * <b>Important:</b><br>
     * By setting <code>withDialog=true</code> the default implementation will
     * try to use a native facebook dialog. If option of native dialog will not
     * succeed, then a web facebook dialog will be used.<br>
     * <br>
     *
     * For having the native dialog, you must add to your <b>manifest.xml</b>
     * 'app_id' meta value:
     *
     * <pre>
     * {@code <}meta-data
     *      android:name="com.facebook.sdk.ApplicationId"
     *      android:value="@string/app_id" /{@code >}
     * </pre>
     *
     * And in your <b>string.xml</b> add your app_id. For example:
     *
     * <pre>
     * {@code <}string name="app_id"{@code >}625994234086470{@code <}/string{@code >}
     * </pre>
     *
     * @param feed
     *            The feed to post
     * @param withDialog
     *            Set <code>True</code> if you want to use dialog.
     * @param onPublishListener
     */
    public void publish(Feed feed, boolean withDialog, OnPublishListener onPublishListener) {
        if (!withDialog) {
            // make it silently
            publish(feed, onPublishListener);
        } else {
            PublishFeedDialogAction publishFeedDialogAction = new PublishFeedDialogAction(mSessionManager);
            publishFeedDialogAction.setFeed(feed);
            publishFeedDialogAction.setOnPublishListener(onPublishListener);
            publishFeedDialogAction.execute();
        }
    }

    /**
     * Publish open graph story.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}
     *
     * @param story
     * @param onPublishListener
     */
    public void publish(Story story, OnPublishListener onPublishListener) {
        publish("me", (Publishable) story, onPublishListener);
    }

    /**
     * Publish open graph story with dialog or without.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}
     *
     * @param story
     * @param onPublishListener
     */
    public void publish(Story story, boolean withDialog, OnPublishListener onPublishListener) {
        if (!withDialog) {
            // make it silently
            publish(story, onPublishListener);
        } else {
            PublishStoryDialogAction publishStoryDialogAction = new PublishStoryDialogAction(mSessionManager);
            publishStoryDialogAction.setStory(story);
            publishStoryDialogAction.setOnPublishListener(onPublishListener);
            publishStoryDialogAction.execute();
        }
    }

    /**
     * Create new album.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * {@link com.software.android.cameraapp.Permission#PUBLISH_ACTION}<br>
     * <br>
     *
     * <b>Important:</b><br>
     * Make sure has {@link com.software.android.cameraapp.Permission#USER_PHOTOS} permission<br>
     * before use this function.<br>
     *
     * @param album
     *            The album to create
     * @param onPublishListener
     *            The callback listener
     */
    public void publish(Album album, OnPublishListener onPublishListener) {
        publish("me", (Publishable) album, onPublishListener);
    }

    /**
     * Publish photo to specific album.
     * <br>
     *
     * <b>Permission:</b><br>
     * Permission#PUBLISH_STREAM<br>
     * <br>
     *
     * <b>Important:</b><br>
     * - The user must own the album<br>
     * - The album should not be full (Max: 200 photos). Check it by
     * {@link Album#getCount()}<br>
     * - The app can add photos to the album<br>
     * - The privacy setting of the app should be at minimum as the privacy
     * setting of the album ( {@link Album#getPrivacy()}
     *
     * @param photo
     *            The photo to upload
     * @param albumId
     *            The album to which the photo should be uploaded
     * @param onPublishListener
     *            The callback listener
     */
    public void publish(Photo photo, String albumId, OnPublishListener onPublishListener) {
        publish(albumId, (Publishable) photo, onPublishListener);
    }

    /**
     * Publish photo to application default album.<br>
     * <br>
     *
     * <b>Permission:</b><br>
     * Permission#PUBLISH_STREAM<br>
     * <br>
     *
     * <b>Important:</b><br>
     * - The album should not be full (Max: 200 photos). Check it by
     * {@link Album#getCount()}<br>
     * {@link Album#getPrivacy()}
     *
     * @param photo
     *            The photo to upload
     * @param onPublishListener
     *            The callback listener
     */
    public void publish(Photo photo, boolean withDialog, OnPublishListener onPublishListener) {
        if (!withDialog) {
            publish("me", (Publishable) photo, onPublishListener);
        } else {
            List<Photo> photos = Utils.createSingleItemList(photo);
            PublishPhotoDialogAction publishPhotoDialogAction = new PublishPhotoDialogAction(mSessionManager);
            publishPhotoDialogAction.setPhotos(photos);
            publishPhotoDialogAction.setPlace(photo.getPlaceId());
            publishPhotoDialogAction.setOnPublishListener(onPublishListener);
            publishPhotoDialogAction.execute();
        }
    }

    /**
     * Publish up to 6 photos to user timeline <b>WITH DIALOG ONLY</b>
     *
     * @param photos
     * @param onPublishListener
     */
    public void publish(List<Photo> photos, OnPublishListener onPublishListener) {
        PublishPhotoDialogAction publishPhotoDialogAction = new PublishPhotoDialogAction(mSessionManager);
        publishPhotoDialogAction.setPhotos(photos);
        // the assumption is that the all photos from the same location. So we
        // take the location from the first photo.
        publishPhotoDialogAction.setPlace(photos.get(0).getPlaceId());
        publishPhotoDialogAction.setOnPublishListener(onPublishListener);
        publishPhotoDialogAction.execute();
    }

    /**
     * Publish video to "Videos" album. <br>
     *
     * <b>Permission:</b><br>
     * Permission#PUBLISH_STREAM<br>
     * <br>
     *
     * @param video
     *            The video to upload
     * @param onPublishListener
     *            The callback listener
     */
    public void publish(Video video, OnPublishListener onPublishListener) {
        publish("me", (Publishable) video, onPublishListener);
    }

    public void publish(byte [] videoByte, Video video, OnPublishListener onPublishListener) {
        publish("me", videoByte, (Publishable) video, onPublishListener);
    }
    public void publish(byte [] videoByte, Video video) {
        publish("me", videoByte, (Publishable) video, video.getFileName());
    }

    /**
     * Publish any publishable entity to target (entity)
     *
     * @param entityId
     * @param publishable
     * @param onPublishListener
     */
    public void publish(String entityId, Publishable publishable, OnPublishListener onPublishListener) {
        PublishAction publishAction = new PublishAction(mSessionManager);
        publishAction.setPublishable(publishable);
        publishAction.setTarget(entityId);
        publishAction.setOnPublishListener(onPublishListener);
        publishAction.execute();
    }
    public void publish(String entityId, byte[] video, Publishable publishable, OnPublishListener onPublishListener) {
        PublishAction publishAction = new PublishAction(mSessionManager);
        publishAction.setVideo(video);
        publishAction.setPublishable(publishable);
        publishAction.setTarget(entityId);
        publishAction.setOnPublishListener(onPublishListener);
        publishAction.execute();
    }
    public void publish(String entityId, byte[] video, Publishable publishable, String fileName) {
        PublishAction publishAction = new PublishAction(mSessionManager);
        publishAction.setFileName(fileName);
        publishAction.setVideo(video);
        publishAction.setPublishable(publishable);
        publishAction.setTarget(entityId);
        publishAction.execute();
    }

    /**
     * Open invite dialog and can add multiple friends
     *
     * @param message
     *            (Optional) The message inside the dialog. It could be
     *            <code>null</code>
     * @param data
     *            (Optional) The data you want to send within the request. It
     *            could be <code>null</code>
     * @param onInviteListener
     *            The listener. It could be <code>null</code>
     */
    public void invite(String message, final OnInviteListener onInviteListener, String data) {
        InviteAction inviteAction = new InviteAction(mSessionManager);
        inviteAction.setMessage(message);
        inviteAction.setData(data);
        inviteAction.setOnInviteListener(onInviteListener);
        inviteAction.execute();
    }

    /**
     * Open invite dialog and invite only specific friend
     *
     * @param to
     *            The id of the friend profile
     * @param message
     *            The message inside the dialog. It could be <code>null</code>
     * @param data
     *            (Optional) The data you want to send within the request. It
     *            could be <code>null</code>
     * @param onInviteListener
     *            The listener. It could be <code>null</code>
     */
    public void invite(String to, String message, final OnInviteListener onInviteListener, String data) {
        InviteAction inviteAction = new InviteAction(mSessionManager);
        inviteAction.setTo(to);
        inviteAction.setMessage(message);
        inviteAction.setData(data);
        inviteAction.setOnInviteListener(onInviteListener);
        inviteAction.execute();
    }

    /**
     * Open invite dialog and invite several specific friends
     *
     * @param suggestedFriends
     *            The ids of friends' profiles
     * @param message
     *            The message inside the dialog. It could be <code>null</code>
     * @param data
     *            (Optional) The data you want to send within the request. It
     *            could be <code>null</code>
     * @param onInviteListener
     *            The error listener. It could be <code>null</code>
     */
    public void invite(String[] suggestedFriends, String message, final OnInviteListener onInviteListener, String data) {
        InviteAction inviteAction = new InviteAction(mSessionManager);
        inviteAction.setSuggestions(suggestedFriends);
        inviteAction.setMessage(message);
        inviteAction.setData(data);
        inviteAction.setOnInviteListener(onInviteListener);
        inviteAction.execute();
    }

    /**
     * Create open graph object on facebook side. <br>
     * <br>
     *
     * <b>What is this method about:</b><br>
     * Objects can be used in two different ways:
     *
     * <li>Self-hosted objects are represented by HTML markup on a particular
     * URL which uniquely defines each object. Using self-hosted objects
     * requires that you host them as pages on your own web server and all
     * self-hosted objects are public.</li>
     *
     * <li>The Object API lets you create and manage Open Graph objects using a
     * simple HTTP-based API, without the requirement for a web server to host
     * them. The Object API can also create objects that have custom or
     * non-public privacy settings and includes an API for you to upload images
     * to Facebook to use in objects and stories.</li><br>
     *
     * <b>This method is the second option, which means, you can create object
     * on facebook servers and reuse it in your app.</b><br>
     * <br>
     *
     * <b>Note:</b><br>
     * You don't need to create the same object any time that user what to share
     * the story. Just reuse the same object, by having the <b>id</b> that you
     * got on response.
     */
    public void create(StoryObject storyObject, OnCreateStoryObject onCreateStoryObject) {
        publish("me", (Publishable) storyObject, onCreateStoryObject);
    }

    /**
     *
     * Deletes an apprequest.<br>
     * <br>
     *
     * @param inRequestId
     *            Input request id to be deleted. Note that it should have the
     *            form {USERID}_{REQUESTID} <code>String</code>
     * @param onDeleteListener
     *            The listener for deletion action
     */
    public void deleteRequest(String inRequestId, final OnDeleteListener onDeleteListener) {
        DeleteRequestAction deleteRequestAction = new DeleteRequestAction(mSessionManager);
        deleteRequestAction.setRequestId(inRequestId);
        deleteRequestAction.setOnDeleteListener(onDeleteListener);
        deleteRequestAction.execute();
    }

    /**
     *
     * Requests any new permission in a runtime. <br>
     * <br>
     * Useful when you just want to request the action and won't be publishing
     * at the time, but still need the updated <b>access token</b> with the
     * permissions (possibly to pass back to your backend).
     *
     * <br>
     * <b>Must be logged to use.</b>
     *
     * @param permissions
     *            New permissions you want to have. This array can include READ
     *            and PUBLISH permissions in the same time. Just ask what you
     *            need.<br>
     * <br>
     * @param onNewPermissionsListener
     *            The listener for the requesting new permission action.
     */
    public void requestNewPermissions(Permission[] permissions, OnNewPermissionsListener onNewPermissionsListener) {
        mSessionManager.requestNewPermissions(permissions, onNewPermissionsListener);
    }

    /**
     * Get the list of all granted permissions. <br>
     * Use {@link com.software.android.cameraapp.Permission#fromValue(String)} to get the {@link com.software.android.cameraapp.Permission}
     * object from string in this list.
     *
     * @return List of granted permissions
     */
    public Set<String> getGrantedPermissions() {
        return mSessionManager.getAcceptedPermissions();
    }

    /**
     * @return <code>True</code> if all permissions were granted by the user,
     *         otherwise return <code>False</code>
     */
    public boolean isAllPermissionsGranted() {
        return mSessionManager.isAllPermissionsGranted();
    }

    /**
     * This is a new feature by facebook, that allows connecting devices and users like smart TV.
     * https://developers.facebook.com/docs/facebook-login/for-devices. <br>
     *
     * This method will return code that user will have to insert in facebook.com/device. This code
     * expires after 420 seconds. While waiting, poll for authorization each 5 seconds to see if user
     * completed the login flow.
     */
    public void connectDevice(Permission[] permissions, OnConnectDeviceListener onConnectDeviceListener) {
        ConnectDeviceAction connectDeviceAction = new ConnectDeviceAction(permissions);
        connectDeviceAction.setActionListener(onConnectDeviceListener);
        connectDeviceAction.execute();
    }

    /**
     * This is a new feature by facebook, that allows connecting devices and users like smart TV.
     * https://developers.facebook.com/docs/facebook-login/for-devices
     *
     * Check the #connectDevice method explanation.
     */
    public void pollDeviceAuthorization(String authorizationCode, OnAuthorizationDeviceListener onAuthorizationDeviceListener) {
        PollDeviceAuthorizationAction pollDeviceAuthorizationAction = new PollDeviceAuthorizationAction(authorizationCode);
        pollDeviceAuthorizationAction.setActionListener(onAuthorizationDeviceListener);
        pollDeviceAuthorizationAction.execute();
    }

    /**
     * Get the current access token
     */
    public AccessToken getAccessToken() {
        return mSessionManager.getAccessToken();
    }

    /**
     * Get string access token
     */
    public String getToken() {
        return mSessionManager.getAccessToken().getToken();
    }

    /**
     * Call this inside your activity in {@link android.app.Activity#onActivityResult}
     * method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSessionManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Clean all references like Activity to prevent memory leaks
     */
    public void clean() {
        mSessionManager.clean();
    }


}
