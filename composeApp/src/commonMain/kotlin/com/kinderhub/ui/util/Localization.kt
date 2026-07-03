package com.kinderhub.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Supported languages in the app
 */
enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    English("en", "English", "English"),
    Irish("ga", "Irish", "Gaeilge"),
    French("fr", "French", "Français");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code == code } ?: English
        }
    }
}

/**
 * Localization state holder
 */
class LocalizationState(initialLanguage: AppLanguage = AppLanguage.English) {
    var currentLanguage by mutableStateOf(initialLanguage)
        private set

    fun setLanguage(language: AppLanguage) {
        currentLanguage = language
    }
}

/**
 * CompositionLocal for accessing localization state
 */
val LocalLocalization = staticCompositionLocalOf { LocalizationState() }

/**
 * Provider for localization
 */
@Composable
fun LocalizationProvider(
    initialLanguage: AppLanguage = AppLanguage.English,
    content: @Composable () -> Unit
) {
    val localizationState = remember { LocalizationState(initialLanguage) }
    CompositionLocalProvider(LocalLocalization provides localizationState) {
        content()
    }
}

/**
 * String resources object - provides localized strings based on current language
 *
 * Usage:
 *   val strings = Strings.current
 *   Text(text = strings.navDiscover)
 */
object Strings {
    val current: AppStrings
        @Composable
        get() {
            val localization = LocalLocalization.current
            return when (localization.currentLanguage) {
                AppLanguage.English -> EnglishStrings
                AppLanguage.Irish -> IrishStrings
                AppLanguage.French -> FrenchStrings
            }
        }
}

/**
 * Interface defining all app strings
 */
interface AppStrings {
    // App
    val appName: String

    // Navigation
    val navDiscover: String
    val navBookings: String
    val navMyBookings: String
    val navMessages: String
    val navAccount: String

    // Auth
    val authWelcomeBack: String
    val authSignInToContinue: String
    val authContinueWithApple: String
    val authContinueWithGoogle: String
    val authOrUseEmail: String
    val authEmail: String
    val authEmailPlaceholder: String
    val authPassword: String
    val authPasswordPlaceholder: String
    val authForgotPassword: String
    val authLogIn: String
    val authSignUp: String
    val authNoAccount: String
    val authHaveAccount: String
    val authTermsPrefix: String
    val authTermsOfService: String
    val authAnd: String
    val authPrivacyPolicy: String

    // Validation
    val validationEmailRequired: String
    val validationEmailInvalid: String
    val validationPasswordRequired: String
    val validationPasswordMinLength: String

    // Onboarding
    val onboardingAddChildren: String
    val onboardingAddChildrenSubtitle: String
    val onboardingFirstName: String
    val onboardingFirstNamePlaceholder: String
    val onboardingDateOfBirth: String
    val onboardingAddAnotherChild: String
    val onboardingContinue: String
    val onboardingSkip: String
    fun onboardingStep(current: Int, total: Int): String

    // Discover
    fun discoverGoodMorning(name: String): String
    fun discoverGoodAfternoon(name: String): String
    fun discoverGoodEvening(name: String): String
    val discoverAllChildren: String
    val discoverAskAi: String
    val discoverAiPlaceholder: String
    val discoverForYou: String
    val discoverFilters: String
    val discoverAiPick: String
    fun discoverLeft(count: Int): String
    val discoverPerSession: String

    // Categories
    val categorySwimming: String
    val categorySteam: String
    val categorySports: String
    val categoryMusic: String
    val categoryTutors: String
    val categoryArt: String
    val categoryDance: String
    val categoryLanguages: String

    // Activity
    val activityAbout: String
    val activitySessions: String
    val activityReviews: String
    val activityLocation: String
    fun activityAges(min: Int, max: Int): String
    val activityBookSession: String
    val activitySelectSession: String
    val activitySelectChild: String
    val activityMessageProvider: String

    // Booking
    val bookingCheckout: String
    val bookingOrderSummary: String
    val bookingSubtotal: String
    val bookingServiceFee: String
    val bookingDiscount: String
    val bookingTotal: String
    val bookingPromoCode: String
    val bookingApply: String
    val bookingPaymentMethod: String
    val bookingPayNow: String
    val bookingProcessing: String

    // Confirmation
    val confirmationBookingConfirmed: String
    fun confirmationNumber(number: String): String
    val confirmationViewBookings: String
    val confirmationBackToDiscover: String

    // Bookings
    val bookingsUpcoming: String
    val bookingsPast: String
    val bookingsNoUpcoming: String
    val bookingsNoPast: String
    val bookingsCancel: String
    val bookingsReschedule: String

    // Messages
    val messagesTitle: String
    val messagesNoMessages: String
    val messagesTypeMessage: String
    val messagesSend: String

    // Account
    val accountTitle: String
    val accountChildren: String
    val accountAddChild: String
    val accountPaymentMethods: String
    val accountAddPayment: String
    val accountSettings: String
    val accountNotifications: String
    val accountPrivacy: String
    val accountAppSettings: String
    val accountSupport: String
    val accountHelp: String
    val accountTerms: String
    val accountLogOut: String
    val accountLoggingOut: String
    fun accountYearsOld(years: Int): String
    val accountDefault: String

    // Common
    val commonLoading: String
    val commonError: String
    val commonRetry: String
    val commonCancel: String
    val commonSave: String
    val commonDelete: String
    val commonEdit: String
    val commonDone: String
    val commonNext: String
    val commonBack: String
    val commonClose: String
    val commonSearch: String
    val commonGoBack: String

    // Units
    val unitMiles: String
    fun unitReviews(count: Int): String
}

/**
 * English strings implementation
 */
object EnglishStrings : AppStrings {
    override val appName = "KinderHub"
    override val navDiscover = "Discover"
    override val navBookings = "Bookings"
    override val navMyBookings = "My Bookings"
    override val navMessages = "Messages"
    override val navAccount = "Account"
    override val authWelcomeBack = "Welcome back"
    override val authSignInToContinue = "Sign in to continue"
    override val authContinueWithApple = "Continue with Apple"
    override val authContinueWithGoogle = "Continue with Google"
    override val authOrUseEmail = "or use email"
    override val authEmail = "Email"
    override val authEmailPlaceholder = "sarah@example.com"
    override val authPassword = "Password"
    override val authPasswordPlaceholder = "Enter your password"
    override val authForgotPassword = "Forgot password?"
    override val authLogIn = "Log in"
    override val authSignUp = "Sign up"
    override val authNoAccount = "Don't have an account?"
    override val authHaveAccount = "Already have an account?"
    override val authTermsPrefix = "By continuing, you agree to our"
    override val authTermsOfService = "Terms of Service"
    override val authAnd = "and"
    override val authPrivacyPolicy = "Privacy Policy"
    override val validationEmailRequired = "Email is required"
    override val validationEmailInvalid = "Please enter a valid email"
    override val validationPasswordRequired = "Password is required"
    override val validationPasswordMinLength = "Password must be at least 8 characters"
    override val onboardingAddChildren = "Add your children"
    override val onboardingAddChildrenSubtitle = "We'll personalise activity recommendations based on their ages."
    override val onboardingFirstName = "First name"
    override val onboardingFirstNamePlaceholder = "e.g. Ella"
    override val onboardingDateOfBirth = "Date of birth"
    override val onboardingAddAnotherChild = "Add another child"
    override val onboardingContinue = "Continue"
    override val onboardingSkip = "Skip"
    override fun onboardingStep(current: Int, total: Int) = "Step $current of $total"
    override fun discoverGoodMorning(name: String) = "Good morning, $name"
    override fun discoverGoodAfternoon(name: String) = "Good afternoon, $name"
    override fun discoverGoodEvening(name: String) = "Good evening, $name"
    override val discoverAllChildren = "All children"
    override val discoverAskAi = "Ask KinderHub AI"
    override val discoverAiPlaceholder = "swimming for my 5-year-old on Saturday mornings near me"
    override val discoverForYou = "For you"
    override val discoverFilters = "Filters"
    override val discoverAiPick = "AI pick"
    override fun discoverLeft(count: Int) = "$count left"
    override val discoverPerSession = "/session"
    override val categorySwimming = "Swimming"
    override val categorySteam = "STEAM"
    override val categorySports = "Sports"
    override val categoryMusic = "Music"
    override val categoryTutors = "Tutors"
    override val categoryArt = "Art"
    override val categoryDance = "Dance"
    override val categoryLanguages = "Languages"
    override val activityAbout = "About"
    override val activitySessions = "Sessions"
    override val activityReviews = "Reviews"
    override val activityLocation = "Location"
    override fun activityAges(min: Int, max: Int) = "Ages $min–$max"
    override val activityBookSession = "Book Session"
    override val activitySelectSession = "Select a session"
    override val activitySelectChild = "Select child"
    override val activityMessageProvider = "Message Provider"
    override val bookingCheckout = "Checkout"
    override val bookingOrderSummary = "Order Summary"
    override val bookingSubtotal = "Subtotal"
    override val bookingServiceFee = "Service fee"
    override val bookingDiscount = "Discount"
    override val bookingTotal = "Total"
    override val bookingPromoCode = "Promo code"
    override val bookingApply = "Apply"
    override val bookingPaymentMethod = "Payment Method"
    override val bookingPayNow = "Pay Now"
    override val bookingProcessing = "Processing..."
    override val confirmationBookingConfirmed = "Booking Confirmed!"
    override fun confirmationNumber(number: String) = "Confirmation #$number"
    override val confirmationViewBookings = "View My Bookings"
    override val confirmationBackToDiscover = "Back to Discover"
    override val bookingsUpcoming = "Upcoming"
    override val bookingsPast = "Past"
    override val bookingsNoUpcoming = "No upcoming bookings"
    override val bookingsNoPast = "No past bookings"
    override val bookingsCancel = "Cancel Booking"
    override val bookingsReschedule = "Reschedule"
    override val messagesTitle = "Messages"
    override val messagesNoMessages = "No messages yet"
    override val messagesTypeMessage = "Type a message..."
    override val messagesSend = "Send"
    override val accountTitle = "Account"
    override val accountChildren = "Children"
    override val accountAddChild = "Add Child"
    override val accountPaymentMethods = "Payment Methods"
    override val accountAddPayment = "Add Payment Method"
    override val accountSettings = "Settings"
    override val accountNotifications = "Notifications"
    override val accountPrivacy = "Privacy & Security"
    override val accountAppSettings = "App Settings"
    override val accountSupport = "Support"
    override val accountHelp = "Help Centre"
    override val accountTerms = "Terms & Privacy Policy"
    override val accountLogOut = "Log Out"
    override val accountLoggingOut = "Logging out..."
    override fun accountYearsOld(years: Int) = "$years years old"
    override val accountDefault = "Default"
    override val commonLoading = "Loading..."
    override val commonError = "Something went wrong"
    override val commonRetry = "Retry"
    override val commonCancel = "Cancel"
    override val commonSave = "Save"
    override val commonDelete = "Delete"
    override val commonEdit = "Edit"
    override val commonDone = "Done"
    override val commonNext = "Next"
    override val commonBack = "Back"
    override val commonClose = "Close"
    override val commonSearch = "Search"
    override val commonGoBack = "Go Back"
    override val unitMiles = "mi"
    override fun unitReviews(count: Int) = "($count)"
}

/**
 * Irish strings implementation
 */
object IrishStrings : AppStrings {
    override val appName = "KinderHub"
    override val navDiscover = "Aimsigh"
    override val navBookings = "Áirithintí"
    override val navMyBookings = "Mo Chuid Áirithintí"
    override val navMessages = "Teachtaireachtaí"
    override val navAccount = "Cuntas"
    override val authWelcomeBack = "Fáilte ar ais"
    override val authSignInToContinue = "Sínigh isteach le leanúint ar aghaidh"
    override val authContinueWithApple = "Lean ar aghaidh le Apple"
    override val authContinueWithGoogle = "Lean ar aghaidh le Google"
    override val authOrUseEmail = "nó úsáid ríomhphost"
    override val authEmail = "Ríomhphost"
    override val authEmailPlaceholder = "sarah@example.com"
    override val authPassword = "Pasfhocal"
    override val authPasswordPlaceholder = "Cuir isteach do phasfhocal"
    override val authForgotPassword = "Pasfhocal dearmadta?"
    override val authLogIn = "Logáil isteach"
    override val authSignUp = "Cláraigh"
    override val authNoAccount = "Níl cuntas agat?"
    override val authHaveAccount = "Tá cuntas agat cheana féin?"
    override val authTermsPrefix = "Trí leanúint ar aghaidh, aontaíonn tú lenár"
    override val authTermsOfService = "Téarmaí Seirbhíse"
    override val authAnd = "agus"
    override val authPrivacyPolicy = "Polasaí Príobháideachta"
    override val validationEmailRequired = "Tá ríomhphost ag teastáil"
    override val validationEmailInvalid = "Cuir isteach ríomhphost bailí"
    override val validationPasswordRequired = "Tá pasfhocal ag teastáil"
    override val validationPasswordMinLength = "Caithfidh an pasfhocal a bheith 8 gcarachtar ar a laghad"
    override val onboardingAddChildren = "Cuir do pháistí leis"
    override val onboardingAddChildrenSubtitle = "Déanfaimid moltaí gníomhaíochtaí a phearsanú bunaithe ar a n-aoiseanna."
    override val onboardingFirstName = "Céadainm"
    override val onboardingFirstNamePlaceholder = "m.sh. Ella"
    override val onboardingDateOfBirth = "Dáta breithe"
    override val onboardingAddAnotherChild = "Cuir páiste eile leis"
    override val onboardingContinue = "Lean ar aghaidh"
    override val onboardingSkip = "Scipeáil"
    override fun onboardingStep(current: Int, total: Int) = "Céim $current de $total"
    override fun discoverGoodMorning(name: String) = "Maidin mhaith, $name"
    override fun discoverGoodAfternoon(name: String) = "Tráthnóna maith, $name"
    override fun discoverGoodEvening(name: String) = "Oíche mhaith, $name"
    override val discoverAllChildren = "Gach páiste"
    override val discoverAskAi = "Fiafraigh de KinderHub AI"
    override val discoverAiPlaceholder = "snámh do mo pháiste 5 bliana maidin Dé Sathairn in aice liom"
    override val discoverForYou = "Duit féin"
    override val discoverFilters = "Scagairí"
    override val discoverAiPick = "Rogha AI"
    override fun discoverLeft(count: Int) = "$count fágtha"
    override val discoverPerSession = "/seisiún"
    override val categorySwimming = "Snámh"
    override val categorySteam = "STEAM"
    override val categorySports = "Spórt"
    override val categoryMusic = "Ceol"
    override val categoryTutors = "Teagascóirí"
    override val categoryArt = "Ealaín"
    override val categoryDance = "Damhsa"
    override val categoryLanguages = "Teangacha"
    override val activityAbout = "Maidir le"
    override val activitySessions = "Seisiúin"
    override val activityReviews = "Léirmheasanna"
    override val activityLocation = "Suíomh"
    override fun activityAges(min: Int, max: Int) = "Aoiseanna $min–$max"
    override val activityBookSession = "Cuir Seisiún in Áirithe"
    override val activitySelectSession = "Roghnaigh seisiún"
    override val activitySelectChild = "Roghnaigh páiste"
    override val activityMessageProvider = "Teachtaireacht chuig Soláthraí"
    override val bookingCheckout = "Seiceáil Amach"
    override val bookingOrderSummary = "Achoimre Ordaithe"
    override val bookingSubtotal = "Fo-iomlán"
    override val bookingServiceFee = "Táille seirbhíse"
    override val bookingDiscount = "Lascaine"
    override val bookingTotal = "Iomlán"
    override val bookingPromoCode = "Cód promóisin"
    override val bookingApply = "Cuir i bhfeidhm"
    override val bookingPaymentMethod = "Modh Íocaíochta"
    override val bookingPayNow = "Íoc Anois"
    override val bookingProcessing = "Ag próiseáil..."
    override val confirmationBookingConfirmed = "Áirithint Deimhnithe!"
    override fun confirmationNumber(number: String) = "Deimhniú #$number"
    override val confirmationViewBookings = "Féach ar Mo Chuid Áirithintí"
    override val confirmationBackToDiscover = "Ar ais go Aimsigh"
    override val bookingsUpcoming = "Le teacht"
    override val bookingsPast = "Caite"
    override val bookingsNoUpcoming = "Níl aon áirithintí le teacht"
    override val bookingsNoPast = "Níl aon áirithintí caite"
    override val bookingsCancel = "Cealaigh Áirithint"
    override val bookingsReschedule = "Athsceideal"
    override val messagesTitle = "Teachtaireachtaí"
    override val messagesNoMessages = "Níl aon teachtaireachtaí fós"
    override val messagesTypeMessage = "Clóscríobh teachtaireacht..."
    override val messagesSend = "Seol"
    override val accountTitle = "Cuntas"
    override val accountChildren = "Páistí"
    override val accountAddChild = "Cuir Páiste Leis"
    override val accountPaymentMethods = "Modhanna Íocaíochta"
    override val accountAddPayment = "Cuir Modh Íocaíochta Leis"
    override val accountSettings = "Socruithe"
    override val accountNotifications = "Fógraí"
    override val accountPrivacy = "Príobháideachas & Slándáil"
    override val accountAppSettings = "Socruithe Aipe"
    override val accountSupport = "Tacaíocht"
    override val accountHelp = "Ionad Cabhrach"
    override val accountTerms = "Téarmaí & Polasaí Príobháideachta"
    override val accountLogOut = "Logáil Amach"
    override val accountLoggingOut = "Ag logáil amach..."
    override fun accountYearsOld(years: Int) = "$years bliain d'aois"
    override val accountDefault = "Réamhshocraithe"
    override val commonLoading = "Ag lódáil..."
    override val commonError = "Chuaigh rud éigin mícheart"
    override val commonRetry = "Bain triail eile as"
    override val commonCancel = "Cealaigh"
    override val commonSave = "Sábháil"
    override val commonDelete = "Scrios"
    override val commonEdit = "Cuir in eagar"
    override val commonDone = "Déanta"
    override val commonNext = "Ar aghaidh"
    override val commonBack = "Ar ais"
    override val commonClose = "Dún"
    override val commonSearch = "Cuardaigh"
    override val commonGoBack = "Téigh ar ais"
    override val unitMiles = "míle"
    override fun unitReviews(count: Int) = "($count)"
}

/**
 * French strings implementation
 */
object FrenchStrings : AppStrings {
    override val appName = "KinderHub"
    override val navDiscover = "Découvrir"
    override val navBookings = "Réservations"
    override val navMyBookings = "Mes Réservations"
    override val navMessages = "Messages"
    override val navAccount = "Compte"
    override val authWelcomeBack = "Bon retour"
    override val authSignInToContinue = "Connectez-vous pour continuer"
    override val authContinueWithApple = "Continuer avec Apple"
    override val authContinueWithGoogle = "Continuer avec Google"
    override val authOrUseEmail = "ou utilisez l'email"
    override val authEmail = "Email"
    override val authEmailPlaceholder = "sarah@example.com"
    override val authPassword = "Mot de passe"
    override val authPasswordPlaceholder = "Entrez votre mot de passe"
    override val authForgotPassword = "Mot de passe oublié ?"
    override val authLogIn = "Se connecter"
    override val authSignUp = "S'inscrire"
    override val authNoAccount = "Vous n'avez pas de compte ?"
    override val authHaveAccount = "Vous avez déjà un compte ?"
    override val authTermsPrefix = "En continuant, vous acceptez nos"
    override val authTermsOfService = "Conditions d'utilisation"
    override val authAnd = "et"
    override val authPrivacyPolicy = "Politique de confidentialité"
    override val validationEmailRequired = "L'email est requis"
    override val validationEmailInvalid = "Veuillez entrer un email valide"
    override val validationPasswordRequired = "Le mot de passe est requis"
    override val validationPasswordMinLength = "Le mot de passe doit contenir au moins 8 caractères"
    override val onboardingAddChildren = "Ajoutez vos enfants"
    override val onboardingAddChildrenSubtitle = "Nous personnaliserons les recommandations d'activités en fonction de leur âge."
    override val onboardingFirstName = "Prénom"
    override val onboardingFirstNamePlaceholder = "ex. Ella"
    override val onboardingDateOfBirth = "Date de naissance"
    override val onboardingAddAnotherChild = "Ajouter un autre enfant"
    override val onboardingContinue = "Continuer"
    override val onboardingSkip = "Passer"
    override fun onboardingStep(current: Int, total: Int) = "Étape $current sur $total"
    override fun discoverGoodMorning(name: String) = "Bonjour, $name"
    override fun discoverGoodAfternoon(name: String) = "Bon après-midi, $name"
    override fun discoverGoodEvening(name: String) = "Bonsoir, $name"
    override val discoverAllChildren = "Tous les enfants"
    override val discoverAskAi = "Demander à KinderHub AI"
    override val discoverAiPlaceholder = "natation pour mon enfant de 5 ans le samedi matin près de chez moi"
    override val discoverForYou = "Pour vous"
    override val discoverFilters = "Filtres"
    override val discoverAiPick = "Choix IA"
    override fun discoverLeft(count: Int) = "$count restant(s)"
    override val discoverPerSession = "/séance"
    override val categorySwimming = "Natation"
    override val categorySteam = "STEAM"
    override val categorySports = "Sports"
    override val categoryMusic = "Musique"
    override val categoryTutors = "Tuteurs"
    override val categoryArt = "Art"
    override val categoryDance = "Danse"
    override val categoryLanguages = "Langues"
    override val activityAbout = "À propos"
    override val activitySessions = "Séances"
    override val activityReviews = "Avis"
    override val activityLocation = "Lieu"
    override fun activityAges(min: Int, max: Int) = "Âges $min–$max"
    override val activityBookSession = "Réserver une séance"
    override val activitySelectSession = "Sélectionner une séance"
    override val activitySelectChild = "Sélectionner un enfant"
    override val activityMessageProvider = "Contacter le prestataire"
    override val bookingCheckout = "Paiement"
    override val bookingOrderSummary = "Récapitulatif"
    override val bookingSubtotal = "Sous-total"
    override val bookingServiceFee = "Frais de service"
    override val bookingDiscount = "Réduction"
    override val bookingTotal = "Total"
    override val bookingPromoCode = "Code promo"
    override val bookingApply = "Appliquer"
    override val bookingPaymentMethod = "Moyen de paiement"
    override val bookingPayNow = "Payer maintenant"
    override val bookingProcessing = "Traitement en cours..."
    override val confirmationBookingConfirmed = "Réservation confirmée !"
    override fun confirmationNumber(number: String) = "Confirmation #$number"
    override val confirmationViewBookings = "Voir mes réservations"
    override val confirmationBackToDiscover = "Retour à Découvrir"
    override val bookingsUpcoming = "À venir"
    override val bookingsPast = "Passées"
    override val bookingsNoUpcoming = "Aucune réservation à venir"
    override val bookingsNoPast = "Aucune réservation passée"
    override val bookingsCancel = "Annuler la réservation"
    override val bookingsReschedule = "Reprogrammer"
    override val messagesTitle = "Messages"
    override val messagesNoMessages = "Aucun message pour l'instant"
    override val messagesTypeMessage = "Tapez un message..."
    override val messagesSend = "Envoyer"
    override val accountTitle = "Compte"
    override val accountChildren = "Enfants"
    override val accountAddChild = "Ajouter un enfant"
    override val accountPaymentMethods = "Moyens de paiement"
    override val accountAddPayment = "Ajouter un moyen de paiement"
    override val accountSettings = "Paramètres"
    override val accountNotifications = "Notifications"
    override val accountPrivacy = "Confidentialité & Sécurité"
    override val accountAppSettings = "Paramètres de l'app"
    override val accountSupport = "Assistance"
    override val accountHelp = "Centre d'aide"
    override val accountTerms = "Conditions & Politique de confidentialité"
    override val accountLogOut = "Déconnexion"
    override val accountLoggingOut = "Déconnexion en cours..."
    override fun accountYearsOld(years: Int) = "$years ans"
    override val accountDefault = "Par défaut"
    override val commonLoading = "Chargement..."
    override val commonError = "Une erreur s'est produite"
    override val commonRetry = "Réessayer"
    override val commonCancel = "Annuler"
    override val commonSave = "Enregistrer"
    override val commonDelete = "Supprimer"
    override val commonEdit = "Modifier"
    override val commonDone = "Terminé"
    override val commonNext = "Suivant"
    override val commonBack = "Retour"
    override val commonClose = "Fermer"
    override val commonSearch = "Rechercher"
    override val commonGoBack = "Retour"
    override val unitMiles = "km"
    override fun unitReviews(count: Int) = "($count)"
}
