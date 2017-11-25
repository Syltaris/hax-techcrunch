
Template.GiftDetails.helpers({

})

Template.GiftDetails.events({
    'click button'(e) {
        e.preventDefault();

        var data = e.target;
        
        var marker_properties = {
            name: Meteor.user().username,
            location: (data.location.value),
            item: data.gift_type.value,
            original_donater: Meteor.user().username,
            gift_visibility_private: data.message_first.value,
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