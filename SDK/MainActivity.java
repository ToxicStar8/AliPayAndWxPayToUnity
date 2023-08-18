package com.unity3d.player;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.alipay.sdk.app.PayTask;

public class MainActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 支付宝App支付
     */
    public void AliPayByApp(final String orderInfo){
        Log.i("Unity", "启动线程");
        Runnable payRun=new Runnable() {
            @Override
            public void run() {
                PayTask task=new PayTask(MainActivity.this);
                String result= task.pay(orderInfo, true);
                Log.i("Unity", "onALIPayFinish, result = " + result);
                //UnityPlayer.UnitySendMessage("GamePay", "ALiPayResult", result);
            }
        };
        Thread payThread = new Thread(payRun);
        payThread.start();
    }

    private IWXAPI msgApi;
    /**
     * 微信App支付
     */
    public void WxPayByApp(String appId,String partnerId,String prepayId,String nonceStr,String timeStamp,String sign){
        msgApi = WXAPIFactory.createWXAPI(this, appId);
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId =partnerId;
        request.prepayId= prepayId;
        request.packageValue = "Sign=WXPay";
        request.nonceStr= nonceStr;
        request.timeStamp= timeStamp;
        request.sign=sign;
        Log.d("Unity",request.checkArgs()+"");//输出验签是否正确
        msgApi.sendReq(request);
    }
}