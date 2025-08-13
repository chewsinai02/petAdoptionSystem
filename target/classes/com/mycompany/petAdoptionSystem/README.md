*Pet adoption system*

Sdg 15

*Function* :
1. Crud for admin manage pet. 
2. ⁠Normal user adopt pet.
3. ⁠user update the well being of pet that adopted for admin.

*Purpose* :
1.
2.
3.

@startuml
' Main entity classes
class Pet {
    - id: int
    - name: String
    - type: String
    - sex: String
    - birthday: Date
    - pic: String
    - state: int
    - remark: String
}

class ManagePetsScreen {
    - petTable: TableView<Pet>
    - nameField: TextField
    - typeField: TextField
    - sexComboBox: ComboBox<String>
    - birthdayPicker: DatePicker
    - picField: TextField
    - stateComboBox: ComboBox<String>
    + ManagePetsScreen(Stage)
}

class ManageAdoptionsScreen {
    - table: TableView<AdoptionRequest>
    + ManageAdoptionsScreen(Stage)
}

class AdoptionRequest {
    - id: int
    - userName: String
    - petName: String
    - adoptTime: String
    - status: String
    - realName: String
    - telephone: String
    - email: String
    - sex: String
    - age: int
    - address: String
    - userState: int
    - petHave: int
    - experience: int
    - pic: String
}

class PetGalleryScreen {
    + PetGalleryScreen(Stage)
}

class PetGalleryPet {
    - id: int
    - petName: String
    - petType: String
    - sex: String
    - birthday: Date
    - pic: String
    - state: int
    - remark: String
}

class UserSession {
    - loggedIn: boolean
    - currentUserId: int
}

class ViewAdoptionApplicationsScreen {
    - table: TableView<AdoptionApplication>
    + ViewAdoptionApplicationsScreen(Stage)
}

class AdoptionApplication {
    - id: int
    - petName: String
    - petType: String
    - applyDate: String
    - status: String
    - remarks: String
}

class ViewPetUpdate {
    - updateTable: TableView<PetUpdate>
    + ViewPetUpdate()
}

class PetUpdate {
    - id: int
    - adoptId: int
    - updateTime: Date
    - updateContent: String
    - updatePic: String
    - remark: String
}

class updatePetStatus {
    - petComboBox: ComboBox<String>
    - updateContent: TextArea
    - picPathField: TextField
    + updatePetStatus()
}

class RegisterScreen {
    - fullNameField: TextField
    - usernameField: TextField
    - emailField: TextField
    - phoneField: TextField
    - ageField: TextField
    - petHaveField: TextField
    - experienceField: TextField
    - genderComboBox: ComboBox<String>
    - profilePicComboBox: ComboBox<String>
    - addressField: TextArea
    - passwordField: PasswordField
    - confirmPasswordField: PasswordField
    + RegisterScreen(Stage)
}

class LoginScreen {
    - loginField: TextField
    - passwordField: PasswordField
    + LoginScreen(Stage)
}

class DatabaseConnection {
    + getConnection(): Connection
}

class AdoptedPetListScreen
class requestPetUpdate
class MainScreen
class AdminDashboardScreen
class Project_GUI
class PetData
class SystemInfo

' Relationships
ManagePetsScreen --> Pet
ManageAdoptionsScreen --> AdoptionRequest
PetGalleryScreen --> PetGalleryPet
PetGalleryScreen --> UserSession
ViewAdoptionApplicationsScreen --> AdoptionApplication
ViewPetUpdate --> PetUpdate

@enduml
