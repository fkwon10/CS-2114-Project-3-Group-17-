package encore.view;

import encore.model.QueueStatusDTO;

public interface QueueObserver {
    void update(QueueStatusDTO status);
    void showError(String message);
}
