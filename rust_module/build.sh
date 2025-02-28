cargo ndk -t armeabi-v7a -t arm64-v8a -t x86 -t x86_64 build --release

mkdir -p ../app/src/main/jniLibs/{armeabi-v7a,arm64-v8a,x86,x86_64}
cp target/armv7-linux-androideabi/release/librust_module.so ../app/src/main/jniLibs/armeabi-v7a/
cp target/aarch64-linux-android/release/librust_module.so ../app/src/main/jniLibs/arm64-v8a/
cp target/i686-linux-android/release/librust_module.so ../app/src/main/jniLibs/x86/
cp target/x86_64-linux-android/release/librust_module.so ../app/src/main/jniLibs/x86_64/
