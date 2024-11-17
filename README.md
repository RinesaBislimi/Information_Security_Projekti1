# **University of Prishtina “Hasan Prishtina”**
## Faculty of Electrical and Computer Engineering

**Level**: Master  
**Course**: Information Security  
**Project Title**: Implementation of FrodoKEM for key encapsulation with errors in Java

##### Team Members
- Diana Beqiri
- Diare Daqi
- Rinesa Bislimi
- Valdrin Ejupi
---

# FrodoKEM Implementation

## Introduction

FrodoKEM is a lattice-based post-quantum cryptographic scheme designed to resist attacks from quantum computers. It is based on the **Learning With Errors (LWE)** problem, a mathematically hard problem that forms the foundation for ensuring the security of this algorithm. FrodoKEM is one of the most studied key encapsulation mechanisms (KEMs) in the NIST Post-Quantum Cryptography Standardization project.

A **Key Encapsulation Mechanism (KEM)** is a cryptographic primitive used to securely exchange symmetric keys over an insecure channel. KEMs are especially useful in hybrid cryptographic systems where they are combined with traditional encryption to future-proof the confidentiality of communications. 

## How FrodoKEM Works

FrodoKEM's security is based on the Learning with Errors (LWE) problem, which introduces noise into linear equations to make the problem infeasible to solve, even with quantum computers. The FrodoKEM process is divided into three main steps:

1. **Key Generation**:
   - The sender and receiver generate a shared public key using random matrices and noise.
   - The receiver also generates a private key.
   - **Formula**:
     $$B = (A \cdot S + E) \mod Q$$
     Where:
     - $$A$$: Randomly generated public matrix
     - $$S$$: Secret matrix
     - $$E$$: Error matrix (Gaussian noise)
     - $$Q$$: Modulus

2. **Encapsulation**:
   - The sender uses the public key to generate a shared symmetric key and ciphertext, which is sent to the receiver.
   - **Formulas**:
     $$C_1 = (A \cdot r + e_1) \mod Q$$
     $$C_2 = (B \cdot r + e_2) \mod Q$$
     Where:
     - $$C_1$$: First part of the ciphertext
     - $$C_2$$: Second part of the ciphertext
     - $$r$$: Random vector
     - $$e_1,e_2$$: Error vectors (Gaussian noise)


3. **Decapsulation**:
- The receiver uses the private key to compute the **Derived Secret Matrix** and then derives the **Shared Secret** from the ciphertext.
 - **Formulas**: Derived Secret Matrix
    $$\text{Derived Secret Matrix} = C_2 - (S^T \cdot C_1) \mod Q$$

    - **Formulas**: Shared Secret
    $$\text{Shared Secret} = \text{Hash}(C_2 - S^T \cdot C_1 \mod Q)$$

    Where:
    -  $$C_2$$: Second part of the ciphertext.
    -  $$S^T$$: Transpose of the private matrix \( S \).
    -  $$C_1$$: First part of the ciphertext.
    - **Hash**: A cryptographic hash function (e.g., SHAKE-256).

The derived shared symmetric key can then be used for further encrypting communications using symmetric encryption schemes (e.g., AES).


### Why FrodoKEM Matters

FrodoKEM is designed to address the need for secure communications in a world where quantum computers could break classical cryptographic schemes such as RSA and ECC. Unlike other lattice-based schemes like Kyber, which rely on structured lattices, FrodoKEM uses unstructured lattices, offering a simpler security model at the cost of larger key sizes.

---

### Visualizing the KEM Mechanism

Below is a diagram illustrating the general structure of a KEM mechanism:

![KEM Mechanism](https://cf-assets.www.cloudflare.com/zkvhlag99gkb/7bbaBxcIILEzrNhIFZNb6p/11e6723fc4c28fdfa43009db7892e9a3/image3-21.png)

- **Key Generation**: The public key is shared, and the private key is securely stored.
- **Encapsulation**: A sender uses the public key to create a ciphertext and a shared secret.
- **Decapsulation**: The receiver derives the shared secret using the private key and the ciphertext.

This diagram highlights the interaction between key encapsulation and decapsulation, which underpins the security of FrodoKEM.

---

## Security Levels

FrodoKEM supports different parameter sets to achieve varying levels of security:

- **FrodoKEM-640**: Targets security equivalent to AES-128. Recommended for general applications requiring good security against quantum attacks.
- **FrodoKEM-976**: Targets security equivalent to AES-192. Suitable for higher security requirements.
- **FrodoKEM-1344**: Targets security equivalent to AES-256. Designed for scenarios demanding the highest level of security against future quantum computers.

## Features Implemented
1. Matrix Operations
2. Discrete Gaussian Noise Sampling
3. SHAKE-256 Hashing
4. Encapsulation/Decapsulation
5. CSPRNG (Cryptographically Secure Pseudo-Random Number Generator)

## Additional Resources
- [Library used in code](https://www.bouncycastle.org/download/bouncy-castle-java/)
- [Algorithm Specifications And Supporting Documentation](https://frodokem.org/files/FrodoKEM-specification-20171130.pdf)


## References
- [Deep dive into a post-quantum key encapsulation algorithm](https://blog.cloudflare.com/post-quantum-key-encapsulation/)
