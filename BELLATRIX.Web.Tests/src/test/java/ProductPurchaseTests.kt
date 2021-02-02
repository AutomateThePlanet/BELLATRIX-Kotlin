import org.testng.annotations.Test
import pages.cartpage.CartPage
import pages.checkoutpage.CheckoutPage
import pages.checkoutpage.PurchaseInfo
import pages.mainpage.MainPage
import solutions.bellatrix.components.Anchor
import solutions.bellatrix.components.Image
import solutions.bellatrix.findstrategies.IdContainingFindStrategy
import solutions.bellatrix.infrastructure.Browser
import solutions.bellatrix.infrastructure.ExecutionBrowser
import solutions.bellatrix.infrastructure.Lifecycle
import solutions.bellatrix.infrastructure.WebTest

@ExecutionBrowser(browser = Browser.CHROME, lifecycle = Lifecycle.REUSE_IF_STARTED)
class ProductPurchaseTests : WebTest() {
    @Test
    fun completePurchaseSuccessfully_first() {
        app.navigate.to("http://demos.bellatrix.solutions/")
        val addToCartFalcon9 = app.create.byCss<Anchor>("[data-product_id*='28']")
        val blogLink = app.create.byCss<Anchor>("[data-product_id*='28']")
        addToCartFalcon9.click()
        blogLink.layout().assertAboveOf(addToCartFalcon9)
        blogLink.layout() assertAboveOf addToCartFalcon9
        MainPage().asserts.productBoxLink("", "")
    }

    @Test
    fun completePurchaseSuccessfully_second() {
        app.navigate.to("http://demos.bellatrix.solutions/")
        val addToCartFalcon9 = app.create.byCss<Anchor>("[data-product_id*='28']")
        addToCartFalcon9.click()
    }

    @Test
    fun falcon9LinkAddsCorrectProduct() {
        val mainPage = app.goTo<MainPage>()
        mainPage.asserts.productBoxLink("Falcon 9", "http://demos.bellatrix.solutions/product/falcon-9/")
    }

    @Test
    fun saturnVLinkAddsCorrectProduct() {
        val mainPage = app.goTo<MainPage>()
        mainPage.asserts.productBoxLink("Saturn V", "http://demos.bellatrix.solutions/product/saturn-v/")
    }

    @Test
    fun purchaseFalcon9WithoutFacade() {
        val mainPage = app.goTo<MainPage>()
        mainPage.addRocketToShoppingCart("Falcon 9")
        val cartPage = app.create<CartPage>()
        cartPage.applyCoupon("happybirthday")
        cartPage.asserts.couponAppliedSuccessfully()
        cartPage.increaseProductQuantity(2)
        cartPage.asserts.totalPrice("114.00€")
        cartPage.clickProceedToCheckout()
        val purchaseInfo = PurchaseInfo()
        purchaseInfo.email = "info@berlinspaceflowers.com"
        purchaseInfo.firstName = "Anton"
        purchaseInfo.lastName = "Angelov"
        purchaseInfo.company = "Space Flowers"
        purchaseInfo.country = "Germany"
        purchaseInfo.address1 = "1 Willi Brandt Avenue Tiergarten"
        purchaseInfo.address2 = "Lьtzowplatz 17"
        purchaseInfo.city = "Berlin"
        purchaseInfo.zip = "10115"
        purchaseInfo.phone = "+00498888999281"

        val checkoutPage = app.create<CheckoutPage>()
        checkoutPage.fillInfo(purchaseInfo)
        checkoutPage.asserts.orderReceived()
    }

    @Test
    fun purchaseSaturnVWithoutFacade() {
        val mainPage = app.goTo<MainPage>()
        mainPage.addRocketToShoppingCart("Saturn V")
        val cartPage = app.create<CartPage>()
        cartPage.applyCoupon("happybirthday")
        cartPage.asserts.couponAppliedSuccessfully()
        cartPage.increaseProductQuantity(3)
        cartPage.asserts.totalPrice("355.00€")
        cartPage.clickProceedToCheckout()
        val purchaseInfo = PurchaseInfo()
        purchaseInfo.email = "info@berlinspaceflowers.com"
        purchaseInfo.firstName = "Anton"
        purchaseInfo.lastName = "Angelov"
        purchaseInfo.company = "Space Flowers"
        purchaseInfo.country = "Germany"
        purchaseInfo.address1 = "1 Willi Brandt Avenue Tiergarten"
        purchaseInfo.address2 = "Lьtzowplatz 17"
        purchaseInfo.city = "Berlin"
        purchaseInfo.zip = "10115"
        purchaseInfo.phone = "+00498888999281"

        val checkoutPage = app.create<CheckoutPage>()
        checkoutPage.fillInfo(purchaseInfo)
        checkoutPage.asserts.orderReceived()
    }
}