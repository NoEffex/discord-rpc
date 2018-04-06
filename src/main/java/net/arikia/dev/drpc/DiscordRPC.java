package net.arikia.dev.drpc;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Nicolas "Vatuu" Adamoglou
 * @version 1.0
 */

public final class DiscordRPC{

    //DLL-Version for Update Check.
    private static final String DLL_VERSION = "3.1.0";
    private static DLL INSTANCE = null;
    
    private static DLL instance() {
        if (INSTANCE == null) {
            INSTANCE = (DLL) Native.loadLibrary("discord-rpc", DLL.class);
        }
        return INSTANCE;
    }

    /**
     * Method to initialize the JNA lib if different from discord-rpc
     * @param libraryName Library name (e.g. discord-rpc64.dll)
     */
    public static void nativeInitialize(String libraryName){
        INSTANCE = (DLL) Native.loadLibrary(libraryName, DLL.class);
    }

    /**
     * Method to initialize the Discord-RPC.
     * @param applicationId ApplicationID/ClientID
     * @param handlers      EventHandlers
     * @param autoRegister  AutoRegister
     */
    public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister){
        instance().Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, null);
    }

    /**
     * Method to register the executable of the application/game.
     * Only applicable when autoRegister in discordInitialize is false.
     *
     * @param applicationId ApplicationID/ClientID
     * @param command Launch Command of the application/game.
     */
    public static void discordRegister(String applicationId, String command){
        instance().Discord_Register(applicationId, command);
    }

    /**
     * Method to initialize the Discord-RPC within a Steam Application.
     * @param applicationId ApplicationID/ClientID
     * @param handlers      EventHandlers
     *                      @see DiscordEventHandlers
     * @param autoRegister  AutoRegister
     * @param steamId       SteamAppID
     */
    public static void discordInitialize(String applicationId, DiscordEventHandlers handlers, boolean autoRegister, String steamId){
        instance().Discord_Initialize(applicationId, handlers, autoRegister ? 1 : 0, steamId);
    }

    /**
     * Method to register the Steam-Executable of the application/game.
     * Only applicable when autoRegister in discordInitializeSteam is false.
     *
     * @param applicationId ApplicationID/ClientID
     * @param steamId SteamID of the application/game.
     */
    public static void discordRegisterSteam(String applicationId, String steamId){
        instance().Discord_RegisterSteamGame(applicationId, steamId);
    }

    /**
     * Method to update the registered EventHandlers, after the initialization was
     * already called.
     * @param handlers DiscordEventHandler object with updated callbacks.
     */
    public static void discordUpdateEventHandlers(DiscordEventHandlers handlers){
        instance().Discord_UpdateHandlers(handlers);
    }

    /**
     * Method to shutdown the Discord-RPC from within the application.
     */
    public static void discordShutdown(){
        instance().Discord_Shutdown();
    }

    /**
     * Method to call Callbacks from within the library.
     * Must be called periodically.
     */
    public static void discordRunCallbacks(){
        instance().Discord_RunCallbacks();
    }

    /**
     * Method to update the DiscordRichPresence of the client.
     * @param presence Instance of DiscordRichPresence
     *                 @see DiscordRichPresence
     */
    public static void discordUpdatePresence(DiscordRichPresence presence){
        instance().Discord_UpdatePresence(presence);
    }

    /**
     * Method to clear(and therefor hide) the DiscordRichPresence until a new
     * presence is applied.
     */
    public static void discordClearPresence(){
        instance().Discord_ClearPresence();
    }

    /**
     * Method to respond to Join/Spectate Callback.
     * @param userId UserID of the user to respond to.
     * @param reply DiscordReply to request.
     *              @see DiscordReply
     */
    public static void discordRespond(String userId, DiscordReply reply){
        instance().Discord_Respond(userId, reply.reply);
    }

    //JNA Interface
    private interface DLL extends Library{
        void Discord_Initialize(String applicationId, DiscordEventHandlers handlers, int autoRegister, String optionalSteamId);
        void Discord_Register(String applicationId, String command);
        void Discord_RegisterSteamGame(String applicationId, String steamId);
        void Discord_UpdateHandlers(DiscordEventHandlers handlers);
        void Discord_Shutdown();
        void Discord_RunCallbacks();
        void Discord_UpdatePresence(DiscordRichPresence presence);
        void Discord_ClearPresence();
        void Discord_Respond(String userId, int reply);
    }
}
