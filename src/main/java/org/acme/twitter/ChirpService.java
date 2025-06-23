package org.acme.twitter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.List;

@ApplicationScoped
public class ChirpService {

    @Inject
    @Channel("chirp-events")
    Emitter<String> chirpEmitter;

    @Transactional
    public Chirp createChirp(TwitterUser author, String content) {
        if (content.length() > 280) throw new IllegalArgumentException("Chirp too long!");

        var chirp = new Chirp();
        chirp.author = author;
        chirp.content = content;
        chirp.persist();

        chirpEmitter.send(String.format("New chirp by %s: %s", author.username, content));
        return chirp;
    }

    public List<Chirp> getAllChirps() {
        return Chirp.findAllOrderedByDate();
    }

    public List<Chirp> getChirpsByUser(TwitterUser twitterUser) {
        return Chirp.findByAuthor(twitterUser);
    }

    @Transactional
    public void likeChirp(Long chirpId) {
        var chirp = Chirp.findById(chirpId);
        if (chirp != null) {
//            chirp.likes++;
            chirp.persist();
        }
    }
}
