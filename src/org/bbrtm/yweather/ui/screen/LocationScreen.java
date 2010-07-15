package org.bbrtm.yweather.ui.screen;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.controller.ForecastController;
import org.bbrtm.yweather.controller.LocationController;
import org.bbrtm.yweather.controller.OptionsController;
import org.bbrtm.yweather.controller.WeatherController;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.ui.dialog.AddLocationDialog;
import org.bbrtm.yweather.ui.dialog.RenameDialog;
import org.bbrtm.yweather.ui.field.ForecastListField;
import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;

public class LocationScreen extends BaseScreen
{
    private static Logger          log                    = Logger.getInstance();
    private LocationController     controller             = null;
    private MenuItem               addLocationMenuItem    = null;
    private MenuItem               removeLocationMenuItem = null;
    private MenuItem               moveUpMenuItem         = null;
    private MenuItem               moveDownMenuItem       = null;
    private MenuItem               forecastMenuItem       = null;
    private MenuItem               homescreenMenuItem     = null;
    private MenuItem               optionsMenuItem        = null;
    private MenuItem               refreshAllMenuItem     = null;
    private MenuItem               refreshMenuItem        = null;
    private MenuItem               renameMenuItem         = null;
    
    private ForecastListField      forecastListField      = null;
    
    private GoogleAnalyticsTracker tracker                = null;
    
    public LocationScreen(LocationController controller)
    {
        super();
        
        tracker = GoogleAnalyticsTracker.getInstance();
        tracker.trackPageView("/locations");
        
        this.controller = controller;
        
        setTitle(new LabelField(resource.getString(YWeatherResource.LOCATIONS_TITLE), USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        Place[] places = controller.getPlaces();
        forecastListField = new ForecastListField();
        forecastListField.set(places);
        forecastListField.setCookie("list");
        forecastListField.setChangeListener(controller);
        forecastListField.setEmptyString(resource.getString(YWeatherResource.LOCATIONS_EMPTY), DrawStyle.HCENTER);
        add(forecastListField);
        
        addLocationMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_ADD_LOCATION, 409000, 2000)
        {
            public void run()
            {
                search();
            }
        };
        
        addMenuItem(addLocationMenuItem);
        
        optionsMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_OPTIONS, 809000, 2000)
        {
            public void run()
            {
                showOptions();
            }
        };
        
        addMenuItem(optionsMenuItem);
        
        homescreenMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_SET_HOMESCREEN, 407000, 2000)
        {
            public void run()
            {
                setHomeScreen();
            }
        };
        
        addMenuItem(homescreenMenuItem);
        
        removeLocationMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_REMOVE_LOCATION, 408000, 2000)
        {
            public void run()
            {
                remove();
            }
        };
        
        addMenuItem(removeLocationMenuItem);
        
        moveUpMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_MOVE_UP, 405000, 2000)
        {
            public void run()
            {
                moveUp();
            }
        };
        
        addMenuItem(moveUpMenuItem);
        
        moveDownMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_MOVE_DOWN, 404000, 2000)
        {
            public void run()
            {
                moveDown();
            }
        };
        
        addMenuItem(moveDownMenuItem);
        
        forecastMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_FORECAST, 600000, 1000)
        {
            public void run()
            {
                viewForecast();
            }
        };
        
        addMenuItem(forecastMenuItem);
        
        refreshMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_REFRESH, 700100, 2000)
        {
            public void run()
            {
                refresh();
                getController().refreshView();
            }
        };
        
        addMenuItem(refreshMenuItem);
        
        refreshAllMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_REFRESH_ALL, 700200, 2000)
        {
            public void run()
            {
                refreshAll();
                getController().refreshView();
            }
        };
        
        addMenuItem(refreshAllMenuItem);
        
        renameMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_RENAME, 408500, 2000)
        {
            public void run()
            {
                rename();
            }
        };
        
        addMenuItem(renameMenuItem);
        
        if (places.length == 0)
        {
            UiApplication.getUiApplication().invokeLater(new Runnable()
            {
                public void run()
                {
                    search();
                }
            });
        }
    }
    
    public LocationController getController()
    {
        return this.controller;
    }
    
    public void close()
    {
        if (Configuration.getInstance().isBackgroundEnabled())
            UiApplication.getUiApplication().requestBackground();
        else
            super.close();
    }
    
    public void setLocations(Place[] places)
    {
        forecastListField.set(places);
        forecastListField.invalidate();
    }
    
    public void setSelectedIndex(int i)
    {
        int size = forecastListField.getSize();
        if (size == 0)
            return;
        
        if (i < 0)
            i = 0;
        else if (i >= size)
            i = size - 1;
        
        forecastListField.setSelectedIndex(i);
    }
    
    public int getSelectedIndex()
    {
        return forecastListField.getSelectedIndex();
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
        if (c == 'r')
        {
            log.debug("refresh location from keyboard");
            refresh();
            return true;
        }
        if (c == 'a')
        {
            log.debug("refresh location from keyboard");
            search();
            return true;
        }
        if (c == 'h')
        {
            log.debug("refresh location from keyboard");
            setHomeScreen();
            return true;
        }
        else if (c == 'R')
        {
            log.debug("refresh all from keyboard");
            refreshAll();
            return true;
        }
        else if (c == 'n')
        {
            log.debug("refresh all from keyboard");
            rename();
            return true;
        }
        else if (c == 'o')
        {
            log.debug("refresh all from keyboard");
            showOptions();
            return true;
        }
        else if (c == Characters.BACKSPACE)
        {
            log.debug("remove location from keyboard");
            remove();
            return true;
        }
        else if (c == Characters.ENTER)
        {
            return navigationClick(status, time);
        }
        else if (c == 'u')
        {
            moveUp();
            return true;
        }
        else if (c == 'd')
        {
            moveDown();
            return true;
        }
        
        return super.keyChar(c, status, time);
    }
    
    private void rename()
    {
        int index = forecastListField.getSelectedIndex();
        
        Place place = (Place) forecastListField.get(forecastListField, index);
        RenameDialog dialog = new RenameDialog(place.getName());
        UiApplication.getUiApplication().pushModalScreen(dialog);
        String newName = dialog.getName();
        if (!newName.equals(place.getName()))
        {
            tracker.trackEvent("Locations", "rename", null, index);
            place.setName(newName);
            getController().updatePlace(place);
        }
    }
    
    private void showOptions()
    {
        OptionsController optionsController = new OptionsController();
        optionsController.showView();
    }
    
    private void moveUp()
    {
        int index = forecastListField.getSelectedIndex();
        tracker.trackEvent("Locations", "move up", null, index);
        getController().moveUp(index);
    }
    
    private void moveDown()
    {
        int index = forecastListField.getSelectedIndex();
        tracker.trackEvent("Locations", "move down", null, index);
        getController().moveDown(index);
    }
    
    private void setHomeScreen()
    {
        int index = forecastListField.getSelectedIndex();
        getController().setHomescreenLocation(index);
        tracker.trackEvent("Locations", "set home screen", null, index);
    }
    
    private void viewForecast()
    {
        int index = forecastListField.getSelectedIndex();
        tracker.trackEvent("Locations", "view", null, index);
        Place place = (Place) forecastListField.get(forecastListField, index);
        if (place != null && place.getWeather() != null)
        {
            ForecastController controller = new ForecastController(place);
            controller.showView();
        }
    }
    
    private void refresh()
    {
        int index = forecastListField.getSelectedIndex();
        tracker.trackEvent("Locations", "refresh", null, index);
        Place place = (Place) forecastListField.get(forecastListField, index);
        WeatherController.getInstance().updateWeather(place);
    }
    
    private void refreshAll()
    {
        tracker.trackEvent("Locations", "refresh all", null, -1);
        WeatherController.getInstance().updateAll();
    }
    
    private void remove()
    {
        int index = forecastListField.getSelectedIndex();
        
        int answer = Dialog.ask(Dialog.D_DELETE, resource.getString(YWeatherResource.DIALOG_REMOVE_LOCATION), Dialog.CANCEL);
        if (answer == Dialog.DELETE)
        {
            getController().removeLocation(index);
            tracker.trackEvent("Locations", "remove", null, index);
        }
    }
    
    private void search()
    {
        tracker.trackEvent("Search", "Search", null, -1);
        AddLocationDialog addDialog = new AddLocationDialog();
        UiApplication.getUiApplication().pushModalScreen(addDialog);
        String location = addDialog.getLocation();
        getController().searchLocation(location);
    }
    
    protected boolean navigationClick(int status, int time)
    {
        int size = forecastListField.getSize();
        if (size == 0)
        {
            search();
            return true;
        }
        else
        {
            viewForecast();
            return true;
        }
    }
}
