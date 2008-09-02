/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_eclipse_actf_util_win32_msaa_MSAA */

#ifndef _Included_org_eclipse_actf_util_win32_msaa_MSAA
#define _Included_org_eclipse_actf_util_win32_msaa_MSAA
#ifdef __cplusplus
extern "C" {
#endif
#undef org_eclipse_actf_util_win32_msaa_MSAA_CHILDID_SELF
#define org_eclipse_actf_util_win32_msaa_MSAA_CHILDID_SELF 0L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_DOWN
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_DOWN 2L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_FIRSTCHILD
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_FIRSTCHILD 7L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_LASTCHILD
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_LASTCHILD 8L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_LEFT
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_LEFT 3L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_NEXT
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_NEXT 5L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_PREVIOUS
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_PREVIOUS 6L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_RIGHT
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_RIGHT 4L
#undef org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_UP
#define org_eclipse_actf_util_win32_msaa_MSAA_NAVDIR_UP 1L
#undef org_eclipse_actf_util_win32_msaa_MSAA_SELFLAG_TAKEFOCUS
#define org_eclipse_actf_util_win32_msaa_MSAA_SELFLAG_TAKEFOCUS 1L
#undef org_eclipse_actf_util_win32_msaa_MSAA_SELFLAG_REMOVESELECTION
#define org_eclipse_actf_util_win32_msaa_MSAA_SELFLAG_REMOVESELECTION 16L
#undef org_eclipse_actf_util_win32_msaa_MSAA_STATE_READONLY
#define org_eclipse_actf_util_win32_msaa_MSAA_STATE_READONLY 64L
#undef org_eclipse_actf_util_win32_msaa_MSAA_STATE_INVISIBLE
#define org_eclipse_actf_util_win32_msaa_MSAA_STATE_INVISIBLE 32768L
#undef org_eclipse_actf_util_win32_msaa_MSAA_STATE_OFFSCREEN
#define org_eclipse_actf_util_win32_msaa_MSAA_STATE_OFFSCREEN 65536L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_WINDOW
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_WINDOW 9L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_CLIENT
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_CLIENT 10L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_LINK
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_LINK 30L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_TEXT
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_TEXT 42L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_PUSHBUTTON
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_PUSHBUTTON 43L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_CHECKBUTTON
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_CHECKBUTTON 44L
#undef org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_RADIOBUTTON
#define org_eclipse_actf_util_win32_msaa_MSAA_ROLE_SYSTEM_RADIOBUTTON 45L
/*
 * Class:     org_eclipse_actf_util_win32_msaa_MSAA
 * Method:    _WindowFromAccessibleObject
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_eclipse_actf_util_win32_msaa_MSAA__1WindowFromAccessibleObject
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_eclipse_actf_util_win32_msaa_MSAA
 * Method:    _getAccessibleChildren
 * Signature: (Lorg/eclipse/actf/util/win32/comclutch/IDispatch;JII)[Ljava/lang/Object;
 */
JNIEXPORT jobjectArray JNICALL Java_org_eclipse_actf_util_win32_msaa_MSAA__1getAccessibleChildren
  (JNIEnv *, jclass, jobject, jlong, jint, jint);

/*
 * Class:     org_eclipse_actf_util_win32_msaa_MSAA
 * Method:    _getRoleText
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_org_eclipse_actf_util_win32_msaa_MSAA__1getRoleText
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_eclipse_actf_util_win32_msaa_MSAA
 * Method:    _AcessibleObjectFromWindow
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_eclipse_actf_util_win32_msaa_MSAA__1AcessibleObjectFromWindow
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
