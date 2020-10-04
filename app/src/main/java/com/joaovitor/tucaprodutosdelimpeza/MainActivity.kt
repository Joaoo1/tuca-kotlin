package com.joaovitor.tucaprodutosdelimpeza

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textview.MaterialTextView
import com.joaovitor.tucaprodutosdelimpeza.data.LoginRepository
import com.joaovitor.tucaprodutosdelimpeza.ui.login.LoginActivity

interface ProgressBarOwner {
    val progressBar: ProgressBar

    fun showProgressBar()
    fun hideProgressBar()
}

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    ProgressBarOwner {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.client_list_fragment,
                R.id.product_list_fragment,
                R.id.sale_list_fragment,
                R.id.reports_fragment,
                R.id.settings_fragment
            ), drawerLayout
        )

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        toolbar.setupWithNavController(navController, appBarConfiguration)

        setupNavigationView()
    }

    private fun setupNavigationView() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        val buttonLogout: AppCompatImageButton = headerView.findViewById(R.id.logout)
        val email: MaterialTextView = headerView.findViewById(R.id.email)
        val name: MaterialTextView = headerView.findViewById(R.id.name)

        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
        email.text = sharedPreferences.getString("userEmail", "")
        name.text = sharedPreferences.getString("userName", "")

        buttonLogout.setOnClickListener {
            LoginRepository(this).logout()
            startActivity(Intent(this, LoginActivity::class.java))
            getSharedPreferences("login", Context.MODE_PRIVATE).edit().clear().apply()
            finish()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)

        return item.onNavDestinationSelected(navHostFragment.navController)
                || super.onOptionsItemSelected(item)
    }

    override val progressBar: ProgressBar
        get() = findViewById(R.id.progress_bar)

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
}