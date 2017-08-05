package org.n_scientific.scientificnoon.ui.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.transition.Explode;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.n_scientific.scientificnoon.Config;
import org.n_scientific.scientificnoon.R;
import org.n_scientific.scientificnoon.ui.BaseActivity;
import org.n_scientific.scientificnoon.ui.noon_members.NoonMembersActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.txtAbout)
    TextView txtAbout;

    @BindView(R.id.btnNoonMembers)
    Button btnNoonMembers;

    @BindView(R.id.btnContactUs)
    Button btnContactUs;

    @BindView(R.id.btnJoinUs)
    Button btnJoinUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        String about = "<br />" +
                "مجموعة نون العلمية، موقع علمي موجه لشرائح المجتمع كافة،تم تأسيس الموقع عام٢٠١٤ م ، تتنوع المشاركات في الموقع بدئاً بالمقالات العلمية وهي نتاج فكري خاص بأعضاء المجموعة المتخصصين بجميع العلوم (الطب،الرياضيات،الفيزياء،الكيمياء،الفلك، الأحياء،الهندسة...وغير ذلك) ومقالات أخرى مترجمة والعديد من الأخبار العلمية و المقاطع المرئية و المسموعة والصور وغيره مراعين دقة المصدر ،مقدمين لأبناء العلم كافة بعيداً عن الحدود الجغرافية محتوى علمي ثري .<br />" +
                "<br />" +
                "<span style=\"color: #800080;\">#أهدافنا :</span><br />" +
                "انطلاقاً من مقولة(العلم ماعلَّمتَه لا ماعَلِمتَه ) نطمح الى،<br />" +
                "١/المساهمة في نشر العلم مستعينين بمنابع العلم من مصادره المحكمة الموثوقة.<br />" +
                "٢/مراعاة ايصال العلم لغير المختصين وذلك بالوضوح بعيداً عن التعقيد.<br />" +
                "٣/محاولة الارتقاء بالمجتمع العربي وذلك بإثراء المحتوى العربي.<br />" +
                "٤/ترجمة العديد من المقالات و الابحاث و الأخبار العلمية.<br />" +
                "٥/تهيئة مجتمع علمي من خلال مواقع التواصل الخاصة بنا و ذلك لتبادل الخبرات و العلوم والاستفادة من ذلك الجو قدر الامكان.<br />" +
                "٦/نعمل كدرع واقِ لمحاربة العلوم المزيفة.<br />" +
                "٧/مايثبته العلم اليوم قد ينفيه غداً لذلك نحن نواكب جديد العلم مراعين الجانب الديني.<br />" +
                "<br />" +
                "<span style=\"color: #800080;\">#رسالتنا :</span><br />" +
                "نهر علمي معلوماتي يفيض إليك تطوعاً من أعضائه من مصادر موثوقة و الغاية هي الرقي بالمجتمع بأسلوب سهل محايد.<br />" +
                "<br />" +
                "<span style=\"color: #800080;\">#رؤيتنا :</span><br />" +
                "<br />" +
                "اكتشاف جوهر العلم لمن لم يكتشفه لبناء مجتمع واعِ في الحاضر و المستقبل ، و قد نتوسع لبلوغ مرام بعيد المدى مستعينين بتكاتف الاعضاء و جهودهم و تعاون المتابعين و قبل ذلك توفيق الله.";
        txtAbout.setText(Html.fromHtml(about));

        btnNoonMembers.setOnClickListener(this);
        btnContactUs.setOnClickListener(this);
        btnJoinUs.setOnClickListener(this);

    }

    @Override
    public int getContentResource() {
        return R.layout.activity_about;
    }

    @Override
    public void injectDependencies() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNoonMembers:

                startActivity(new Intent(this, NoonMembersActivity.class));

                break;

            case R.id.btnContactUs:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Config.CONTACT_US_URL));
                startActivity(intent);
                break;

            case R.id.btnJoinUs:
                Intent joinUs = new Intent(Intent.ACTION_VIEW);
                joinUs.setData(Uri.parse(Config.JOIN_US_URL));
                startActivity(joinUs);

                break;


        }
    }
}
