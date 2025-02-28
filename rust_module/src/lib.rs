use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jstring;

#[unsafe(no_mangle)]
pub extern "C" fn Java_com_kylecorry_trail_1sense_RustBridge_hello(
    mut env: JNIEnv,
    _: JClass,
    input: JString,
) -> jstring {
    let input_str = env.get_string(&input).expect("Couldn't get Java string!");
    let output = format!("Hello from Rust, {}!", input_str.to_string_lossy());
    env.new_string(output)
        .expect("Couldn't create Java string!")
        .into_raw()
}
