package com.yjplay.yjplayer.widge;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yjplay.yjplayer.R;


/**
 * Created by slc on 2017/6/19.
 */
public class LoadingDailog extends Dialog {

    private Context mcontent;
    public LoadingDailog(Context context) {
        super(context, R.style.MyDialogStyle);
        this.mcontent = context;
    }

    public LoadingDailog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public Configurator setMessage(String message){
        return new Configurator(message,this.mcontent);
    }
    public class Configurator{
        private String message;
        private Context context;
        private boolean isCancelable;
        private boolean isCancelOutside;
        private Configurator(String message, Context context){
            this.message = message;
            this.context = context;
        }
        /**
         * 设置是否可以按返回键取消
         * @param isCancelable
         * @return
         */

        public Configurator setCancelable(boolean isCancelable){
            this.isCancelable=isCancelable;
            return this;
        }

        /**
         * 设置是否可以取消
         * @param isCancelOutside
         * @return
         */
        public Configurator setCancelOutside(boolean isCancelOutside){
            this.isCancelOutside=isCancelOutside;
            return this;
        }
        public LoadingDailog create(){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view=inflater.inflate(R.layout.dialog_loading,null);
            TextView msgText= (TextView) view.findViewById(R.id.tipTextView);
            msgText.setText(message);
            LoadingDailog.this.setContentView(view);
            LoadingDailog.this.setCancelable(isCancelable);
            LoadingDailog.this.setCanceledOnTouchOutside(isCancelOutside);
            return LoadingDailog.this;
        }

    }
    public LoadingDailog showis(){
        LoadingDailog.this.show();
        return LoadingDailog.this;
    }
}
