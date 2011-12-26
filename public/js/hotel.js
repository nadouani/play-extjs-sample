Ext.ns('App', 'App.hotel');

App.hotel.Grid = Ext.extend(Ext.grid.GridPanel, {
	renderTo: 'hotel-grid',
	iconCls: 'silk-hotel',
    frame: true,
    title: 'Hotels',
    height: 300,
    width: 800,
    style: 'margin-top: 10px',
    editor : new Ext.ux.grid.RowEditor({
    	saveText: 'Update',
    	clicksToEdit :2
	}),

    initComponent : function() {

        // typical viewConfig
        this.viewConfig = {
            forceFit: true
        };
        
        this.selModel = new Ext.grid.RowSelectionModel({
        	singleSelect: false
        });

        // build toolbars and buttons.
        this.cm = this.buildColumnModel();
        this.tbar = this.buildTopToolbar();
        this.bbar = this.buildBottomToolbar();
        
        this.plugins = [this.editor]
        
        // super
        App.hotel.Grid.superclass.initComponent.call(this);
    },
    
    buildColumnModel : function(){
    	var defaultEditor = new Ext.form.TextField({
            allowBlank: false
    	});
    	
    	return new Ext.grid.ColumnModel({
	    	defaults: {
	          sortable: true // columns are not sortable by default           
	      	},
	      	columns: [
  	          	new Ext.grid.RowNumberer(),	      	          
	      	    { header: '#', readOnly: true, dataIndex: "id", width: 40, hidden: false},
	      	    { header: 'Name', dataIndex: "name", width:150, editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
	      	    { header: 'Website', dataIndex: "website", width:150, editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: 'Phone', dataIndex: "phoneNumber", editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: 'City', dataIndex: "city", editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: 'Country', dataIndex: "country", editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: '# Stars', dataIndex: "stars", width:50, editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: '# Rooms', dataIndex: "rooms", width:60, editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
				{ header: '# Beds', dataIndex: "beds", width:50, editor: {
	                xtype: 'textfield',
	                allowBlank: false
	            }},
	        ]
	    })
    },

    buildTopToolbar : function() {
        return [{
            text: 'Add',
            iconCls: 'silk-add',
            handler: this.onAdd,
            scope: this
        }, '-', {
            text: 'Delete',
            iconCls: 'silk-delete',
            handler: this.onDelete,
            scope: this
        }, 
        '->', 
        {
            text: 'Save',
            iconCls: 'silk-save',
            handler: this.onSave,
            scope: this
        }];
    },

    buildBottomToolbar : function() {
    	return new Ext.PagingToolbar({
            pageSize: 25,
            store: this.store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            items:[
                '-',
                {
                    text: 'autoSave',            
                    iconCls: 'silk-auto-save',
                    enableToggle: true,
                    pressed: false,
                    tooltip: 'When enabled, Store will execute Ajax requests as soon as a Record becomes dirty.',
                    toggleHandler: function(btn, pressed) {
                        this.store.autoSave = pressed;
                    },
                    scope: this
                }, 
                {
                    text: 'batch',
                    iconCls: 'silk-batch',
                    enableToggle: true,
                    tooltip: 'When enabled, Store will batch all records for each type of CRUD verb into a single Ajax request.',
                    toggleHandler: function(btn, pressed) {
                        this.store.batch = pressed;
                    },
                    scope: this
                }
            ]
    	});
    	
    },

    onSave : function(btn, ev) {
        this.store.save();
    },

    onAdd : function(btn, ev) {
        var u = new this.store.recordType({
            name : '',
            website: '',
            phoneNumber : '',
            city : '',
            country : '',
            stars : '',
            rooms : '',
            beds : ''
        });
        this.editor.stopEditing();
        this.store.insert(0, u);
        this.editor.startEditing(0);
    },

    onDelete : function(btn, ev) {
    	var rec = this.getSelectionModel().getSelections();
        if (!rec) {
            return false;
        }
        this.store.remove(rec);
        this.store.save();
    }
});


Ext.onReady(function() {
    Ext.QuickTips.init();

	var reader = new Ext.data.JsonReader({
		idProperty: 'id',
		root: 'data',
		messageProperty: 'message',
		successProperty: 'success',
		fields : [
			{name: 'id'},
			{name: 'name', type: 'string'},
			{name: 'website', type: 'string'},
			{name: 'phoneNumber', type: 'string'},
			{name: 'city', type: 'string'},
			{name: 'country', type: 'string'},
			{name: 'stars', type: 'int'},
			{name: 'rooms', type: 'int'},
			{name: 'beds', type: 'int'}
		]
	});

    var writer = new Ext.data.JsonWriter({
  		writeAllFields: false,
  		listful : true,
  		encode: false,
  		writeAllFields: true
  	});
    
    var hotelStore = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            api: {
	            read : 'hotels/list',
	            create : 'hotels/create',
	            update: 'hotels/update',
	            destroy: 'hotels/delete'
	        }
        }),
  		reader: reader,
  		writer: writer,
  		autoSave: false,
  		sortInfo:{field: 'id', direction: 'ASC'}
    });
    
    hotelStore.load();
    
    var hotelGrid = new App.hotel.Grid({
        renderTo: 'hotel-grid',
        store: hotelStore
    });
    
	Ext.data.DataProxy.addListener('write', function(proxy, action, result, res, rs) {
		Ext.Msg.alert('Status', res.message);

		hotelGrid.getView().refresh();
	});

	Ext.data.DataProxy.addListener('exception', function(proxy, type, action, options, res) {
		Ext.Msg.alert('Exception', "Something bad happend while executing " + action);
	});
});
