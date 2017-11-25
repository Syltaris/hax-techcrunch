
Meteor.publish('userPosts', function() {
    return GiftMarkers.find();
})