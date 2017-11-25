
Template.GiftDetails.helpers({

})

Template.GiftDetails.events({
    'click button'(e) {
        e.preventDefault();

        var data = e.target;
        
        var marker_properties = {
            name: Meteor.user().username,
            location: ($(e.target).find('[id=lng_coords]').val(), $(e.target).find('[id=lat_coords]').val()),
            item: $(data).find('[id=gift_type]').val(),
            original_donater: Meteor.user().username,
            gift_visibility_private: 'public',
        };

        console.log(marker_properties);

		Meteor.call('insertMarker', marker_properties, function(error, result) {
			console.log(result);
			if (error) {
				alert(error.reason);
			}
		});
    }
})