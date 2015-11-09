package com.ikut.utils;

public class Constant {
	
	/** TAG */
	public static final String TAG = "TAG";
	public static final String SECRET_KEY = "01234567";
	
	/** SplashScren*/
	public static final int SPLASH_SCREEN_DELAY = 2000;
	
	/** Account*/
    public static final String ADD_ACCOUNT_TYPE = "com.ikut.account";
    public static final String AUTH_TOKEN_TYPE = "AuthTokenType";
    
    /** Cloud */
    // Google project id
    public static final String SENDER_ID = "867229832530"; 
    public static final String GOOGLE_CLOUD_MESSENGER   = "GCM"; 
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;		
    static final String SERVER_URL = "http://10.0.2.2/gcm_server_php/register.php";    
    
	/** Login */
	public static final String ID_SELECT_FRAGMENT = "ID";
	
	/** Code Verification*/
	public static final String FORGET_PASSWORD = "forget_password";
	public static final String FORGET_PASSWORD_OK = "forget_password_ok";

	
	/** Intents and Intent Filters */
	public static final String REGISTER_USER = "registrar";
	public static final String LOGIAR = "logiar";
	public static final String CHANGE_PASSWORD = "change_password";
	public static final String CHANGE_PHONE = "changer_phone";
	public static final String PASSWORD_RECONET = "password_reconet";
	public static final String PHONE_RECONET = "phone_reconet";
	public static final String OK = "ok";    
	
	/** List Drawer */
	public static final int FRAGMENT_LIST_MENSSAGER = 0;
	public static final int FRAGMENT_LIST_MESSENGER_USER = 1;	
	public static final int FRAGMENT_SETTING = 2;
	public static final int FRAGMENT_HELP = 3;
	public static final int EXIT = 4;	
	public static final int FRAGMENT_SEND_MESSENGER = 5;
	public static final int FRAGMENT_PASSWORD_CHANGE = 6;
	public static final int FRAGMENT_PHONE_CHANGE = 7;
	public static final int FRAGMENT_FORGET_PASSWORD = 8;
	public static final int FRAGMENT_PASSWORD = 9;
	public static final int FRAGMENT_DETAIL_MESSENGER = 10;
	/** service */
	public static final String ACTION_START = "com.ikut.START";
	public static final String ACTION_STOP = "com.ikut.STOP";
	public static final String ACTION_RECONNECT = "com.ikut.RECONNECT";
	public static final String TAG_SERVICE = "Service";
	
	/** http */
	public static final int TIME_OUT_CONNECTION = 60000;
	public static final int TIME_OUT_SOCKET  = 60000;
	
	/** WebFunctions*/
	public static String URL = "http://ikutapp.hol.es/android_ikut/index.php";
	public static String LOGIN_URL = "http://ikutapp.hol.es/android_ikut/index.php";
	public static String REGISTER_URL = "http://ikutapp.hol.es/android_ikut/index.php";
	public static String TAG_WEB_FUNCTIONS = "conectar";
	public static String TAG_JSONPARSER = "JSONParser";
	public static String LOGINT_TAG = "checkLogin";
	public static String REGISTER_TAG = "register";
	public static String JSON_TAG = "JSON";
	public static String TAG_BUFFER_ERROR = "Buffer Error";
	public static String TAG_JSON_PARSER = "JSON Parser";
		
	public static String KEY_SUCCESS = "success";
	public static String KEY_ERROR = "error";
	public static String KEY_ERROR_MSG = "error_msg";
	public static String KEY_NOMBRE = "nombre";
	public static String KEY_EMAIL = "email";
	public static String KEY_TELEFONO = "telefono";
	public static String KEY_FECHA_REGISTRO = "fecha_registro";
	public static String KEY_FECHA_MODIFICACION= "fecha_modificacion";
	public static String KEY_ESTADO = "estado";
	public static String KEY_CREATED_AT = "created_at";
	public static String KEY_PASSWORD = "password";
	
	/** Drawer */
	public static final String LAST_POSITION = "LAST_POSITION";
	public static final String FRAGMENT_VIEWPAGER = "FRAGMENT_VIEWPAGER";	
	public static final String FRAGMENT_MYAPPS = "FRAGMENT_MYAPPS";			
	public static final String FRAGMENT_SHOPAPPS = "FRAGMENT_SHOPAPPS";		
	
	public static final String TRY_CATCH = "e-try-catch";	
	
	/** DataBases*/
	public static final String DATABASE = "Ikut";	
	public static final int VERSION_ = 1;
	public static final int VERSION = 1;
	
	/** table mensajes */
	public static final String TABLE_MENSSEGER = "Mensajes";
	public static final String ID_PHONE = "id_phone";
	public static final String ID_SERVER = "id_server";
	public static final String HORA = "hora";
	public static final String FECHA_ENVIADO = "fecha_enviado";
	public static final String ASUNTO = "asunto";
	public static final String CONTENIDO = "contenido";
	public static final String EMAIL = "email";
	public static final String BORRAR = "borrar";
	
	/** table mensajes usuarios */
	public static final String TABLE_MENSSEGER_USER = "Mensajes_User";
	public static final String DELETE_MENSAJES = "Delete_Mensajes_User";
	
			
	/**Fragment send meseenger */
	public static final String MESSENGER_SEND = "com.ikut.send.messenger";
	public static final String SEND = "com.ikut.send";
	public static final String DOWNLOAD_MESSENGER = "com.ikut.dowmload";
	public static final String NO_DOWNLOAD_MESSENGER = "com.ikut.dowmload.no";

}//end Class
