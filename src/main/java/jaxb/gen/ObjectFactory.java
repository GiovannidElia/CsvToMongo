//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.11 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2020.02.28 alle 11:27:50 AM CET 
//


package jaxb.gen;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the jaxb.gen package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _UserRequest_QNAME = new QName("/jaxb/gen", "userRequest");
    private final static QName _UserResponse_QNAME = new QName("/jaxb/gen", "userResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: jaxb.gen
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserRequest }
     * 
     */
    public UserRequest createUserRequest() {
        return new UserRequest();
    }

    /**
     * Create an instance of {@link UserResponse }
     * 
     */
    public UserResponse createUserResponse() {
        return new UserResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "/jaxb/gen", name = "userRequest")
    public JAXBElement<UserRequest> createUserRequest(UserRequest value) {
        return new JAXBElement<UserRequest>(_UserRequest_QNAME, UserRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "/jaxb/gen", name = "userResponse")
    public JAXBElement<UserResponse> createUserResponse(UserResponse value) {
        return new JAXBElement<UserResponse>(_UserResponse_QNAME, UserResponse.class, null, value);
    }

}
