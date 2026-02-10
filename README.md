> **⚠️ Archived Project (2014)** — This is a historical portfolio piece, not actively maintained. Not recommended for forking or production use.

# MagPack - Digital Magazine Distribution Platform (2014)

A comprehensive web-based digital magazine distribution platform built with Java EE, featuring magazine publishing, PDF conversion, mobile app backend, and analytics dashboard.

## Overview

MagPack is a full-stack digital magazine platform that enables publishers to upload, convert, and distribute magazines to mobile users. The system includes an admin panel for publishers, a REST API for mobile applications, PDF-to-image conversion pipeline, and analytics dashboard for tracking readership.

**Development Period:** 2014
**Technology:** Java EE (JSF, JAX-RS, Hibernate)
**Database:** MySQL
**Type:** Enterprise Web Application (WAR deployment)

## Core Features

### Publisher Portal (JSF/PrimeFaces)
- **Magazine Management:** Upload and manage magazine issues (PDF format)
- **Publisher Administration:** Publisher registration and profile management
- **Classification System:** Categorize magazines by topic/genre
- **Page Type Definition:** Define special page types (ads, covers, content pages)
- **Banner Configuration:** Manage promotional banners
- **Analytics Dashboard:** View readership statistics and charts

### Mobile Backend (REST API)
- **User Authentication:** Mobile user login/registration
- **Magazine Catalog:** Browse available magazines by category
- **Purchase System:** Buy magazines and manage subscriptions
- **PDF Delivery:** Serve magazine pages to mobile apps
- **Bookmarks:** Save and sync reading positions across devices
- **Favorites:** Mark magazines as favorites
- **Push Notifications:** GCM (Google Cloud Messaging) integration

### PDF Processing Pipeline
- **Automatic Conversion:** PDF to high-quality images (JPEG/PNG)
- **Multiple Libraries:** Foxit GSDK, PDFBox, Ghost4J, pdf-renderer
- **Background Processing:** Asynchronous conversion using threads
- **Quality Control:** Configurable DPI and image format

### Analytics & Reporting
- **Page View Tracking:** Track which pages users read
- **Geographic Distribution:** User location analytics (GeoChart)
- **Age Demographics:** User age distribution charts
- **Issue Statistics:** Per-issue readership metrics
- **Time-Based Analysis:** Temporal reading patterns

## Technology Stack

### Frontend (Admin Panel)
- **JSF (JavaServer Faces):** Apache MyFaces 2.2
- **PrimeFaces 5.1:** Rich UI component library
- **JSTL:** JSP Standard Tag Library
- **HTML5 + CSS3:** Modern web standards

### Backend
- **JAX-RS (Jersey 2.x):** RESTful web services
- **Hibernate:** Object-relational mapping
- **Servlet API 3.1:** Web container
- **Java EE 7:** Enterprise platform

### PDF Processing
- **Foxit GSDK:** Commercial PDF renderer (local library)
- **Apache PDFBox 1.8.5:** PDF manipulation
- **Ghost4J 1.0.0:** Ghostscript wrapper
- **pdf-renderer 1.0.5:** Pure Java PDF renderer

### Database
- **MySQL 5.6+:** Primary data store
- **UTF-8 Encoding:** Full Unicode support (Persian/Farsi)
- **Connection Pooling:** Efficient database connections

### Push Notifications
- **Google Cloud Messaging (GCM):** Android push notifications
- **Registration Management:** Device token storage and updates

### Build & Deployment
- **Maven 3:** Dependency management and build
- **WAR Packaging:** Servlet container deployment
- **Tomcat/GlassFish:** Compatible application servers

## Project Structure

```
magpack/
├── src/
│   └── main/
│       ├── java/
│       │   └── ir/
│       │       └── mjm/
│       │           ├── admin/              # Admin panel managed beans
│       │           │   ├── AddPublisherView.java
│       │           │   ├── AdminControler.java
│       │           │   ├── MagazineSelectionWizard.java
│       │           │   ├── BannerConfig.java
│       │           │   └── ClassificationOfMagazines.java
│       │           ├── charts/             # Analytics and charting
│       │           │   ├── AgeChart.java
│       │           │   ├── GeoChart.java
│       │           │   ├── PageViewHorizontalChart.java
│       │           │   └── SpecIssuePageCharts.java
│       │           ├── DBAO/               # Data Access Objects
│       │           │   ├── DBFacade.java
│       │           │   ├── ConverterThread.java
│       │           │   └── hiber/          # Hibernate entities
│       │           ├── entities/           # Domain models
│       │           ├── filtering/          # Request filters
│       │           ├── gcmservice/         # Push notification service
│       │           ├── publisher/          # Publisher portal beans
│       │           ├── restBeans/          # REST response objects
│       │           ├── restpkg/            # REST API endpoints
│       │           └── util/               # Utility classes
│       ├── resources/
│       │   └── hibernate.cfg.xml
│       └── webapp/
│           ├── WEB-INF/
│           │   ├── web.xml
│           │   └── faces-config.xml
│           ├── admin/                      # Admin panel XHTML pages
│           ├── publisher/                  # Publisher portal pages
│           ├── resources/                  # CSS, JS, images
│           └── index.xhtml
├── lib/
│   └── fsdk.jar                           # Foxit GSDK (proprietary)
├── wiki/
│   ├── db.sql                             # Database schema
│   └── README.docx                        # Original documentation
├── pom.xml                                # Maven configuration
└── magazine.iml                           # IntelliJ IDEA project
```

## Database Schema

### Core Tables
- **`categories_tbl`**: Magazine categories/genres
- **`magazine_tbl`**: Magazine metadata (title, publisher, description)
- **`publishers_tbl`**: Publisher accounts and profiles
- **`mobileuser`**: Mobile app users
- **`customerownedissues`**: User purchases and subscriptions
- **`issuepagebookmark`**: Reading bookmarks per user/page
- **`pageview_tbl`**: Analytics tracking (page views)
- **`gcm_registration_id`**: GCM device tokens
- **`banners_tbl`**: Promotional banner configuration

### Key Relationships
- Publishers → Magazines (one-to-many)
- Users → Purchased Issues (many-to-many with metadata)
- Issues → Bookmarks (one-to-many per user)
- Users → GCM Tokens (one-to-many for multiple devices)

## REST API Endpoints

### Authentication
```
POST /api/user/login
POST /api/user/register
```

### Magazine Catalog
```
GET  /api/magazines              # List all magazines
GET  /api/magazines/{id}         # Get magazine details
GET  /api/categories             # List categories
GET  /api/magazines/category/{catId}  # Magazines by category
```

### Purchase & Access
```
POST /api/purchase               # Buy a magazine
GET  /api/user/library           # User's owned magazines
GET  /api/magazine/{id}/pages    # Get magazine pages (images)
```

### Bookmarks & Favorites
```
POST /api/bookmark               # Add bookmark
GET  /api/bookmarks              # Get user bookmarks
POST /api/favorite               # Mark as favorite
```

### Push Notifications
```
POST /api/gcm/register           # Register device token
POST /api/gcm/send               # Send notification (admin)
```

## Building the Project

### Prerequisites
- **Java JDK:** 1.7 or higher
- **Maven:** 3.0+
- **MySQL Server:** 5.6+
- **Application Server:** Tomcat 8+ or GlassFish 4+

### Database Setup
```bash
# Import database schema
mysql -u root -p < wiki/db.sql

# Configure connection in src/main/resources/hibernate.cfg.xml
# Update: <property name="connection.url">
#         <property name="connection.username">
#         <property name="connection.password">
```

### Build
```bash
# Clean and compile
mvn clean compile

# Run tests (if available)
mvn test

# Package WAR file
mvn package

# Output: target/magazine-1.0.war
```

### Deployment
```bash
# Tomcat
cp target/magazine-1.0.war $TOMCAT_HOME/webapps/

# GlassFish
asadmin deploy target/magazine-1.0.war

# Access at: http://localhost:8080/magazine-1.0/
```

## Configuration

### Hibernate Database Connection
Edit `src/main/resources/hibernate.cfg.xml`:
```xml
<property name="connection.url">jdbc:mysql://localhost:3306/magazinedb</property>
<property name="connection.username">root</property>
<property name="connection.password">yourpassword</property>
```

### File Upload Settings
Configure in `web.xml`:
- **Max File Size:** For magazine PDF uploads
- **Upload Directory:** Temporary storage path
- **Conversion Output:** Image storage location

### GCM Configuration
Set GCM API key in push notification service:
```java
// ir.mjm.gcmservice package
private static final String GCM_API_KEY = "your-gcm-api-key";
```

## Usage Workflow

### Publisher Workflow
1. **Register:** Create publisher account via admin panel
2. **Upload Magazine:** Upload PDF file through publisher portal
3. **Conversion:** System converts PDF to images automatically
4. **Classification:** Assign categories and metadata
5. **Publish:** Make available to mobile users
6. **Analytics:** Monitor readership and engagement

### Mobile User Workflow
1. **Browse:** Explore magazine catalog by category
2. **Purchase:** Buy individual issues or subscribe
3. **Read:** View magazine pages (served as images)
4. **Bookmark:** Save reading position
5. **Sync:** Bookmarks sync across devices

### Admin Workflow
1. **User Management:** Approve/disable publishers and mobile users
2. **Content Moderation:** Review uploaded magazines
3. **Banner Management:** Configure promotional content
4. **Analytics:** View platform-wide statistics
5. **Push Notifications:** Send announcements to users

## Key Features Implementation

### PDF to Image Conversion
Multi-library fallback approach:
1. Try Foxit GSDK (fastest, highest quality)
2. Fallback to Ghost4J if Foxit fails
3. Fallback to PDFBox if Ghost4J unavailable
4. Final fallback to pdf-renderer (pure Java)

Conversion runs in background thread to avoid blocking uploads.

### Analytics System
- **Real-time Tracking:** Page view events logged immediately
- **Aggregation:** Daily/weekly/monthly rollups
- **Chart Generation:** PrimeFaces Charts component
- **Export:** Analytics data exportable to Excel (Apache POI)

### Multi-tenancy
- Publishers isolated by `pub_id`
- Users can only access purchased content
- Admin role has global access

## Known Dependencies

### Proprietary Library
- **Foxit GSDK (`lib/fsdk.jar`)**: Commercial PDF SDK
  - Not included in public repositories
  - License required for production use
  - Can be replaced with open-source alternatives

### Third-Party Services
- **GCM (Google Cloud Messaging)**: Requires Google account and API key
  - **Note:** GCM has been replaced by FCM (Firebase Cloud Messaging) in 2019
  - Migration to FCM recommended for modern deployments

## Version History

**2015-10-20:** Final Maven build configuration
**2015-10-19:** Initial .gitignore setup
**2015-07-27:** Documentation (README.docx) created
**2015-05-24:** Database schema finalized

## Skills Demonstrated

| Category | Skills |
|----------|--------|
| **Backend Development** | Java EE 7, JAX-RS, Hibernate ORM, Servlet API |
| **Frontend Development** | JSF 2.2, PrimeFaces, XHTML templates |
| **API Design** | RESTful services, JSON serialization, mobile backend |
| **Database** | MySQL, schema design, connection pooling |
| **Document Processing** | PDF rendering, image conversion, multi-library fallback |
| **Enterprise Patterns** | Multi-tier architecture, DAO pattern, dependency injection |
| **Build & Deploy** | Maven, WAR packaging, Tomcat/GlassFish deployment |

## License

MIT License (see LICENSE file)

## Credits

**Development:** 2014
**Organization:** ir.mjm (domain identifier)
**Stack:** Java EE 7, JSF 2.2, JAX-RS 2.0, Hibernate, MySQL
**UI Framework:** PrimeFaces 5.1
**Target Market:** Persian/Farsi digital magazine distribution

---

*Archived 2014 enterprise Java project demonstrating full-stack web development with Java EE, RESTful API design, PDF processing, and mobile backend architecture.*
