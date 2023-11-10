package nb_.mbti_talk;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.io.InputStream;
// Android에서 이미지를 로드하고 표시하기 위해 Glide 라이브러리를 FB Storage와 통합하는 데 사용

// FB storage 이미지를 Glide로 불러오기 위한 클래스
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry){
        // FirebaseImageLoader.Factory를 사용하여 FB Storage의 이미지를 로드할 때 필요한 구성을 설정
        // StorageReference 클래스를 InputStream 클래스로 변환하는 데 사용
        registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }
}
