# Secure Image Encryption - Java Version

A secure image encryption and decryption application built with Java and JavaFX, featuring a modern graphical user interface and robust security features.

## Features

- **Dual-Layer Security**: Combines pixel shifting with AES-128 encryption
- **Modern GUI**: Clean, intuitive JavaFX interface
- **PIN Strength Validation**: Real-time PIN strength assessment
- **Integrity Protection**: SHA256 hash verification
- **Comprehensive Logging**: CSV-based activity logging with authentication
- **File Management**: Automatic cleanup of encrypted files after decryption
- **Statistics & Charts**: Visual representation of encryption/decryption metrics

## Security Features

1. **Pixel Shifting**: Position-based pixel value obfuscation
2. **AES-128 Encryption**: Industry-standard cryptographic encryption
3. **SHA256 Integrity**: Hash-based file integrity verification
4. **PIN Strength Checking**: Real-time password strength assessment
5. **Secure Cleanup**: Automatic deletion of encrypted files after successful decryption

## Requirements

- Java 11 or higher
- Maven 3.6 or higher
- JavaFX (included in dependencies)

## Installation

1. **Clone or download the project**
2. **Navigate to project directory**
3. **Build the project**:
   ```bash
   mvn clean compile
   ```

## Usage

### Running the Application

```bash
mvn javafx:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/secure-image-encryption-1.0.0.jar
```

### Encryption Process

1. Click "Browse Original Image" to select an image file (JPG, JPEG, PNG)
2. Enter a PIN for encryption (strength indicator will show in real-time)
3. Click "Encrypt Image" to start the encryption process
4. Choose where to save the encrypted file (.enc format)
5. View encryption statistics and charts

### Decryption Process

1. Click "Browse Encrypted File" to select a .enc file
2. Enter the correct PIN used for encryption
3. Click "Decrypt Image" to start the decryption process
4. Choose where to save the decrypted image
5. View decryption statistics and charts

### Activity Log

1. Click "View Activity Log" to access the log viewer
2. Enter credentials (default: admin/admin123)
3. View comprehensive activity log with timestamps

## Configuration

Edit `config.properties` to change log access credentials:
```properties
log.username=your_username
log.password=your_password
```

## Project Structure

```
src/main/java/com/imageencryption/
├── MainGUI.java              # Main JavaFX application
├── ImageEncryption.java      # Encryption logic
├── ImageDecryption.java      # Decryption logic
├── KeyUtils.java            # PIN strength and key generation
├── PixelShift.java          # Pixel shifting algorithms
├── ImageUtils.java          # Image processing utilities
├── HashUtils.java           # SHA256 hash generation
├── FileUtils.java           # File operations
├── LogManager.java          # Activity logging
├── ConfigManager.java       # Configuration management
└── ChartGenerator.java      # Statistics visualization
```

## Technical Details

### Encryption Algorithm

1. **Pixel Shifting**: `shift_val = (height * width - (i * width + j)) % 256`
2. **Key Generation**: SHA256 hash of PIN → AES-128 key
3. **Encryption**: AES-128 encryption of shifted image data
4. **Integrity**: SHA256 hash stored in .meta file

### File Formats

- **Input**: JPG, JPEG, PNG
- **Encrypted**: .enc files
- **Metadata**: .meta files (hash storage)
- **Output**: PNG format

### Security Considerations

- Uses industry-standard AES-128 encryption
- SHA256 for integrity verification
- Automatic cleanup prevents file recovery
- PIN strength validation encourages strong passwords
- Comprehensive audit trail

## Dependencies

- **JavaFX**: Modern GUI framework
- **Java Cryptography**: Built-in encryption capabilities
- **Java Image I/O**: Image processing
- **SLF4J/Logback**: Logging framework

## Building from Source

```bash
# Clean and compile
mvn clean compile

# Run tests
mvn test

# Create executable JAR
mvn clean package

# Run with JavaFX
mvn javafx:run
```

## Troubleshooting

### Common Issues

1. **JavaFX not found**: Ensure Java 11+ is installed
2. **Permission errors**: Check file write permissions
3. **Memory issues**: Increase JVM heap size if needed

### Log Files

Activity logs are stored in `logs/activity_log.csv` with timestamps and detailed event information.

## Developed By:
1. **Varigonda Lakshmi Srinivas** 
2. **Pattel Pranay Reddy**

## License

This project is provided as-is for educational and personal use.

## Contributing

Feel free to submit issues, feature requests, or pull requests to improve the application.


