
GiftMarkers = new Mongo.Collection('gift_markers');

Meteor.methods({
    insertMarker: function(marker_properties) {
        GiftMarkers.insert({
            name: '',
            location: '',
            item: '',
            original_donater: '',
        })
    }
})