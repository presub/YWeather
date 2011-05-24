package org.bbrtm.yweather.ui.screen;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.ObjectListField;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.controller.SearchController;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.ui.dialog.AddLocationDialog;
import org.bbrtm.yweather.ui.field.LocationListField;

public class SearchScreen extends BaseScreen
{
    private SearchController  controller          = null;
    private LocationListField resultsListField    = null;
    private ButtonField       loadMoreButtonField = null;
    
    private MenuItem          addLocationMenuItem = null;
    private GoogleAnalyticsTracker tracker = null;
    
    public SearchScreen(SearchController controller)
    {
        super();
        this.controller = controller;
        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.trackPageView("/search");
        this.setTitle(resource.getString(YWeatherResource.SEARCH_TITLE));
        
        resultsListField = new LocationListField();
        resultsListField.setEmptyString(resource.getString(YWeatherResource.SEARCH_EMPTY_STRING), DrawStyle.HCENTER);
        resultsListField.setChangeListener(controller);
        resultsListField.setCookie("list");
        add(resultsListField);
        
        addLocationMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_NEW_SEARCH, 409000, 2000)
        {
            public void run()
            {
                tracker.trackEvent("Search", "New Search", null, -1);
                AddLocationDialog addDialog = new AddLocationDialog();
                UiApplication.getUiApplication().pushModalScreen(addDialog);
                String location = addDialog.getLocation();
                getController().setLocation(location);
                getController().startSearch();
            }
        };
        
        addMenuItem(addLocationMenuItem);
    }
    
    public SearchController getController()
    {
        return this.controller;
    }
    
    public void setLocations(Place[] places)
    {
        resultsListField.set(places);
    }
    
    public void showLoadMoreButton()
    {
        if (loadMoreButtonField == null)
        {
            tracker.trackEvent("Search", "Load More", null, -1);
            loadMoreButtonField = new ButtonField(resource.getString(YWeatherResource.SEARCH_LOAD_MORE_BUTTON), ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
            loadMoreButtonField.setCookie("load");
            loadMoreButtonField.setChangeListener(controller);
            add(loadMoreButtonField);
        }
    }
    
    public void hideLoadMoreButton()
    {
        if (loadMoreButtonField != null)
        {
            delete(loadMoreButtonField);
            loadMoreButtonField = null;
        }
    }
}
