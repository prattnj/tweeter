package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.observer_interface.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter extends Presenter {

    private final UserService service;
    private final View view;

    public RegisterPresenter(View view) {
        this.view = view;
        this.service = new UserService();
    }

    public interface View extends Presenter.View {
        void navigateToMain(User user);
    }

    public class RegisterObserver extends UserObserver {

        @Override
        public void handleSuccess(User user) {
            view.displayMessage("Hello " + Cache.getInstance().getCurrUser().getName());
            view.navigateToMain(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayMessage("Exception encountered.");
            exception.printStackTrace();
        }
    }



    // EXTRA PRESENTER FUNCTIONS

    public void initiateRegister(String firstName, String lastName, String alias, String password, Drawable original) {
        validateRegistration(firstName, lastName, alias, password, original);
        String imageBytesBase64 = convertImage(original);
        service.register(firstName, lastName, alias, password, imageBytesBase64, new RegisterObserver());
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password, Drawable imageToUpload) {
        if (firstName.length() == 0) throw new IllegalArgumentException("First Name cannot be empty.");
        if (lastName.length() == 0) throw new IllegalArgumentException("Last Name cannot be empty.");
        if (alias.length() == 0) throw new IllegalArgumentException("Alias cannot be empty.");
        if (alias.charAt(0) != '@') throw new IllegalArgumentException("Alias must begin with @.");
        if (alias.length() < 2) throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        if (password.length() == 0) throw new IllegalArgumentException("Password cannot be empty.");
        if (imageToUpload == null) throw new IllegalArgumentException("Profile image must be uploaded.");
    }

    public String convertImage(Drawable original) {
        // Convert image to byte array.
        Bitmap image = ((BitmapDrawable) original).getBitmap();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();
        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        return Base64.getEncoder().encodeToString(imageBytes);
    }

}
