//
//  ContactsHelper.swift
//  iosApp
//
//  Created by Admin on 06/03/2025.
//  Copyright © 2025 orgName. All rights reserved.
//
import SwiftUI
import ContactsUI

@objc public class ContactsHelper: NSObject, CNContactPickerDelegate {
    
    private var callback: ((String) -> Void)?

    @objc public override init() {
        super.init()
    }
    
    @objc public func loadContacts(callback: @escaping (String) -> Void) {
        print("Contacts loader invoked")
        self.callback = callback
        
        guard let rootVC = UIApplication.shared.windows.first?.rootViewController else {
            print("Root view controller not found")
            return
        }
        
        let contactPicker = CNContactPickerViewController()
        contactPicker.delegate = self
        contactPicker.displayedPropertyKeys = [CNContactPhoneNumbersKey]
        rootVC.present(contactPicker, animated: true, completion: nil)
    }
    
    public func contactPicker(_ picker: CNContactPickerViewController, didSelect contact: CNContact) {
        if let phoneNumber = contact.phoneNumbers.first?.value.stringValue {
            print("Selected Phone Number: \(phoneNumber)")
            callback?(phoneNumber)
        }
    }
    
    public func contactPickerDidCancel(_ picker: CNContactPickerViewController) {
        print("Contact selection cancelled")
    }
}
