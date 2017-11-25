import React from 'react';
import {
  AsyncStorage,
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  TextInput,
  // ListView,
  // Alert,
  // Button
} from 'react-native';
import { StackNavigator } from 'react-navigation';
// import { Location, Permissions, MapView } from 'expo';
import Swiper from 'react-native-swiper';

//
// export default class App extends React.Component {
//   render() {
//     return (
//       <View style={styles.container}>
//         <Text>Open up App.js to start working on your app!</Text>
//         <Text>Changes you make will automatically reload.</Text>
//         <Text>Shake your phone to open the developer menu.</Text>
//       </View>
//     );
//   }
// }
//
// const styles = StyleSheet.create({
//   container: {
//     flex: 1,
//     backgroundColor: '#fff',
//     alignItems: 'center',
//     justifyContent: 'center',
//   },
// });



class SwiperScreen extends React.Component {
  static navigationOptions = {
    //Change name!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    title: 'My Food Diary!'
  };
  render() {
    return (
      <Swiper>
        <CameraScreen/>
        <AvatarScreen/>
        <FoodlogScreen/>
      </Swiper>
    )
  }
}


//Screens
class LoginScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password: '',
      message: ''
    }
  }

  static navigationOptions = {
    title: 'Login'
  };

  login() {
    if (this.state.password === null || this.state.username === null) {
      this.setState = {
        message: "Please enter both username and password!"
      }
    } else {
      AsyncStorage.getItem('user')
        .then(result => {
          let parsedResult = JSON.parse(result);
          if (parsedResult.username !== null && parsedResult.password !== null) {
            console.log("Successfully found in AsyncStorage");
            if (this.state.username === parsedResult.username && this.state.password === parsedResult.password)
              console.log("Fields match so navigating to main menu!");
              this.setState = {
                message: "Login Successful!"
              };
              this.props.navigation.navigate('Swiper');
            }
          })
        .catch(err => {
          console.log("Error during AsyncStorage retrieval", err);
          this.setState({
            message: 'Login failed :( Please try again!'
          })
        });
    }
  }

  register() {
    this.props.navigation.navigate('Register');
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>{this.state.message}</Text>
        <Text style={styles.textBig}>Welcome to HALP!</Text>
        <TextInput
          style={styles.textBox}
          placeholder="Enter your username"
          onChangeText={(text) => this.setState({username: text})}
        />
        <TextInput
          style={styles.textBox}
          placeholder="Enter your password"
          secureTextEntry={true}
          onChangeText={(text) => this.setState({password: text})}
        />
        <TouchableOpacity onPress={ () => {this.login()} } style={[styles.button, styles.buttonGreen]}>
          <Text style={styles.buttonLabel}>Tap to Login</Text>
        </TouchableOpacity>
        <TouchableOpacity style={[styles.button, styles.buttonBlue]} onPress={ () => {this.register()} }>
          <Text style={styles.buttonLabel}>Tap to Register</Text>
        </TouchableOpacity>
      </View>
    )
  }
}

class RegisterScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      username: '',
      password:'',
      message: ''
    }
  }

  static navigationOptions = {
    title: 'Register'
  };

  submitRegistration() {
    AsyncStorage.setItem('user', JSON.stringify({
        username: this.state.username,
        password: this.state.password
      }))
    .then(() => {
      this.setState({
        message: 'Registration successful!'
      });
      this.props.navigation.navigate('Swiper');
      // this.props.navigation.goBack();
    })
    .catch((error) => {
      console.log("Error dealing saving to AsyncStorage");
      this.setState({
        message: 'Registration failed :( Please try again!!'
      });
    })
      // console.log("Is successful..? ", responseJson.success);
      // this.props.navigation.navigate('Login');
  }

  render() {
    return (
      <View style={styles.container}>
        <Text>{this.state.message}</Text>
        <TextInput
          style={styles.textBox}
          placeholder="Enter your username"
          onChangeText={(text) => this.setState({username: text})}
        />
        <TextInput
          style={styles.textBox}
          placeholder="Enter your password"
          secureTextEntry={true}
          onChangeText={(text) => this.setState({password: text})}
        />
        <View style={[styles.button, styles.buttonBlue]}>
          <TouchableOpacity onPress={() => {this.submitRegistration()}}>
            <Text style={styles.buttonLabel}>Register</Text>
          </TouchableOpacity>
        </View>
      </View>
    )
  }
}

class AvatarScreen extends React.Component {


  // componentDidMount() {
  //   //Use to retrieve recommendation message to be displayed
  //
  //   //Also connect to chatbot
  // }

  static navgiationOptions = ({ navigation }) => ({
    title: 'Avatar',
  })

  render() {
      return (
        <View>
          <Text>Avatar!</Text>
        </View>
      );
  }
}

class FoodlogScreen extends React.Component {

    static navgiationOptions = ({ navigation }) => ({
      title: 'Foodlog',
    })

    render() {
        return (
          <View>
            <Text>Hi!</Text>
          </View>
        );
    }
}

class CameraScreen extends React.Component {

    static navgiationOptions = ({ navigation }) => ({
      title: 'Camera',
    })

    render() {
        return (
          <View>
            <Text>Hi!</Text>
          </View>
        );
    }
}


//Navigator
export default StackNavigator({
  Login: {
    screen: LoginScreen,
  },
  Register: {
    screen: RegisterScreen,
  },
  Avatar: {
    screen: AvatarScreen,
  },
  Foodlog: {
    screen:  FoodlogScreen,
  },
  Camera: {
    screen: CameraScreen,
  },
  Swiper: {
    screen: SwiperScreen,
  }
}, {initialRouteName: 'Login'});


//Styles
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#e8ffff',
  },
  containerFull: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'stretch',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  textBig: {
    fontSize: 36,
    textAlign: 'center',
    margin: 10,
    marginBottom: 20
  },
  button: {
    alignSelf: 'stretch',
    paddingTop: 10,
    paddingBottom: 10,
    marginTop: 10,
    marginLeft: 25,
    marginRight: 25,
    borderRadius: 5
  },
  buttonRed: {
    backgroundColor: '#FF585B',
  },
  buttonBlue: {
    backgroundColor: '#0074D9',
  },
  buttonGreen: {
    backgroundColor: '#2ECC40'
  },
  buttonLabel: {
    textAlign: 'center',
    fontSize: 20,
    color: 'white'
  },
  textBox: {
    height: 40,
    marginLeft: 20, marginRight: 20, marginBottom: 10,
    width: 280,
    justifyContent: 'center', alignItems: 'center',
    borderWidth: 2, borderRadius: 7, textAlign: 'center',
    backgroundColor: '#fafafa'
  },
  userRow: {
    borderBottomWidth: 3,
    marginTop: 5,
    marginBottom: 5,
    minHeight: 30,
    minWidth: 180,
  },
  messageRow: {
    borderBottomWidth: 3,
    marginTop: 5,
    marginBottom: 5,
    minHeight: 30,
    minWidth: 300,
  },
  map: {
    width: 350,
    height: 200,
    marginTop: 15,
    marginBottom: 20
  },
});
