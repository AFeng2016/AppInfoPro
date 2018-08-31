package t.app.info.base;

import android.view.View;

/**
 * detail: Fragment 基类 -- 必须实现的方法和事件
 * Created by Ttt
 */
public abstract class BaseFragment extends IBaseFragment implements View.OnClickListener {

    // ==== 其他对象 ====

    @Override
    public void initValues() {
        super.initValues();
    }

    @Override
    public void onClick(View v) {
    }

    /** 滑动到顶部 */
    public void onScrollTop(){
    }
}
