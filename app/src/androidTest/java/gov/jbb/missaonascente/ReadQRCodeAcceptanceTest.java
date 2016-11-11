package gov.jbb.missaonascente;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;

import gov.jbb.missaonascente.controller.LoginController;
import gov.jbb.missaonascente.controller.RegisterExplorerController;
import gov.jbb.missaonascente.dao.BookDAO;
import gov.jbb.missaonascente.dao.ElementDAO;
import gov.jbb.missaonascente.dao.ExplorerDAO;
import gov.jbb.missaonascente.model.Explorer;
import gov.jbb.missaonascente.view.MainScreenActivity;

import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)

public class ReadQRCodeAcceptanceTest {
    private final ActivityTestRule<MainScreenActivity> main = new ActivityTestRule<>(MainScreenActivity.class);
    private final Context context = InstrumentationRegistry.getTargetContext();;
    private final String EMAIL = "user3@user.com";
    private final String PASSWORD = "000000";
    private final String NICKNAME = "testUser3";

    @Before
    public void setup(){
        ExplorerDAO databaseExplorer = new ExplorerDAO(context);
        databaseExplorer.onUpgrade(databaseExplorer.getWritableDatabase(), 1, 1);
        BookDAO databaseBook = new BookDAO(context);
        databaseBook.onUpgrade(databaseBook.getWritableDatabase(), 1, 1);
        ElementDAO databaseElement = new ElementDAO(context);
        databaseElement.onUpgrade(databaseElement.getWritableDatabase(), 1, 1);
        RegisterExplorerController register = new RegisterExplorerController();
        register.register(NICKNAME, EMAIL, PASSWORD, PASSWORD, context);
        LoginController login = new LoginController();
        login.doLogin(EMAIL, PASSWORD, context);
        while(!login.isAction());
    }

    @Test
    public void testIfQRCodeButtonWasClicked(){
        main.launchActivity(new Intent());

        onView(withId(R.id.readQrCodeButton))
                .perform(click())
                .inRoot(isPopupWindow());

        new ExplorerDAO(context).deleteExplorer(new Explorer(EMAIL, PASSWORD));
    }

    public static Matcher<Root> isPopupWindow() {
        return isPlatformPopup();
    }

}
