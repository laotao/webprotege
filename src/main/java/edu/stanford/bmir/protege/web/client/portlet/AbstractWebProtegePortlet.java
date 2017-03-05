package edu.stanford.bmir.protege.web.client.portlet;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.web.bindery.event.shared.HandlerRegistration;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.bmir.protege.web.shared.user.UserId;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedEvent;
import edu.stanford.protege.widgetmap.client.view.ViewTitleChangedHandler;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Preconditions.checkNotNull;


public abstract class AbstractWebProtegePortlet implements WebProtegePortlet {

    private final SelectionModel selectionModel;

    private final List<HandlerRegistration> handlerRegistrations = new ArrayList<>();

    private final ProjectId projectId;

    public AbstractWebProtegePortlet(@Nonnull SelectionModel selectionModel,
                                     @Nonnull ProjectId projectId) {
        this.selectionModel = checkNotNull(selectionModel);
        this.projectId = checkNotNull(projectId);

        // TODO: Move this out
        HandlerRegistration handlerRegistration = selectionModel.addSelectionChangedHandler(e -> {
//                if (portletUi.asWidget().isAttached()) {
                    handleBeforeSetEntity(e.getPreviousSelection());
                    handleAfterSetEntity(e.getLastSelection());
//                }
            }
        );
        handlerRegistrations.add(handlerRegistration);
    }

    public SelectionModel getSelectionModel() {
        return selectionModel;
    }

    public ProjectId getProjectId() {
        return projectId;
    }

    protected void handleBeforeSetEntity(Optional<? extends OWLEntity> existingEntity) {
    }

    protected void handleAfterSetEntity(Optional<OWLEntity> entityData) {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



//    /**
//     * Adds an event handler to the main event bus.  When the portlet is destroyed the handler will automatically be
//     * removed.
//     * @param type The type of event to listen for.  Not {@code null}.
//     * @param handler The handler for the event type. Not {@code null}.
//     * @param <T> The event type
//     * @throws NullPointerException if any parameters are {@code null}.
//     */
//    public <T> void addProjectEventHandler(Event.Type<T> type, T handler) {
//        HandlerRegistration reg = eventBus.addHandlerToSource(checkNotNull(type), getProjectId(), checkNotNull(handler));
//        handlerRegistrations.add(reg);
//    }
//
//    public <T> void addApplicationEventHandler(Event.Type<T> type, T handler) {
//        HandlerRegistration reg = eventBus.addHandler(checkNotNull(type), checkNotNull(handler));
//        handlerRegistrations.add(reg);
//    }

    private void removeHandlers() {
        for (HandlerRegistration reg : handlerRegistrations) {
            reg.removeHandler();
        }
    }

    public Optional<OWLEntity> getSelectedEntity() {
        return getSelectionModel().getSelection();
    }


    @Override
    public String toString() {
        return toStringHelper("EntityPortlet")
                .addValue(getClass().getName())
                .toString();
    }

    @Override
    public void dispose() {
        removeHandlers();
    }

    private String title = "";

    private HandlerManager handlerManager = new HandlerManager(this);

//    public void setTitle(String title) {
//        fireEvent(new ViewTitleChangedEvent(title));
//    }

    @Override
    public com.google.gwt.event.shared.HandlerRegistration addViewTitleChangedHandler(ViewTitleChangedHandler viewTitleChangedHandler) {
        return handlerManager.addHandler(ViewTitleChangedEvent.getType(), viewTitleChangedHandler);
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent) {
        handlerManager.fireEvent(gwtEvent);
    }
}
